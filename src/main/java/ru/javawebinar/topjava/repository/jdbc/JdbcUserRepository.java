package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final String INSERT_ROLES_QUERY = "INSERT INTO user_roles (user_id, role) VALUES (?,?)";

    private final String REMOVE_ROLES_QUERY = "DELETE FROM user_roles ur WHERE ur.user_id = ? AND ur.role = ?";

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        batchUpdateRoles(user);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users u WHERE id=?", ROW_MAPPER, id);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return setRoles(DataAccessUtils.singleResult(users));
    }

    @Override
    public List<User> getAll() {
        return setAllRoles(jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER));
    }

    /* ------------------------------------------------------------------------------------------------- */

    private User setRoles(User user) {
        if (user != null) {
            Set<Role> roles = getRoles(user);
            user.setRoles(roles);
        }
        return user;
    }

        private List<User> setAllRoles(List<User> users) {
        if (!users.isEmpty()) {
            Map<Integer, List<Role>> roles = jdbcTemplate.query("SELECT * FROM user_roles", new ResultSetExtractor<Map<Integer, List<Role>>>() {
                @Override
                public Map<Integer, List<Role>> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    Map<Integer, List<Role>> roleMap = new HashMap<>();
                    while (rs.next()) {
                        int userId = rs.getInt(1);
                        Role role = Role.valueOf(rs.getString(2));
                        roleMap.computeIfAbsent(userId, roleList -> new ArrayList<>()).add(role);
                    }
                    return roleMap;
                }
            });
            users.forEach(user -> user.setRoles(roles.get(user.getId())));
        }
        return users;
    }

    private void batchUpdateRoles(User user) {
        Set<Role> oldRoles = getRoles(user);
        Set<Role> updatedRoles = user.getRoles();
        if (oldRoles.isEmpty()) {
            batchUpdate(user, updatedRoles, INSERT_ROLES_QUERY);
        } else if (!oldRoles.equals(updatedRoles)) {
            Set<Role> toInsert = new HashSet<>(updatedRoles);
            toInsert.removeAll(oldRoles);
            Set<Role> toRemove = new HashSet<>(oldRoles);
            toRemove.removeAll(updatedRoles);
            if (!toInsert.isEmpty()) {
                batchUpdate(user, toInsert, INSERT_ROLES_QUERY);
            }
            if (!toRemove.isEmpty()) {
                batchUpdate(user, toRemove, REMOVE_ROLES_QUERY);
            }
        }
    }

    private Set<Role> getRoles(User user) {
        return jdbcTemplate.query("SELECT role FROM user_roles ur WHERE ur.user_id=?", new ResultSetExtractor<Set<Role>>() {
            @Override
            public Set<Role> extractData(ResultSet rs) throws SQLException, DataAccessException {
                Set<Role> roleSet = new HashSet<>();
                while (rs.next()) {
                    roleSet.add(Role.valueOf(rs.getString(1)));
                }
                return roleSet;
            }
        }, user.getId());
    }

    private void batchUpdate(User user, Set<Role> roles, String sql) {
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            List<Role> updatedRoles = new ArrayList<>(roles);
            int userId = user.getId();

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setString(2, updatedRoles.get(i).name());
            }

            @Override
            public int getBatchSize() {
                return updatedRoles.size();
            }
        });
    }
}
