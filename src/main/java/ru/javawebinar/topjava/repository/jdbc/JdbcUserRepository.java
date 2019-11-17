package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Transactional(readOnly = true)
@Repository
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

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
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) != 0) {
            jdbcTemplate.execute(String.format("DELETE FROM user_roles ur WHERE ur.user_id = %d", user.getId()));
        } else {
            return null;
        }
        Set<Role> updatedRoles = user.getRoles();
        batchUpdate(user, updatedRoles, "INSERT INTO user_roles (user_id, role) VALUES (?,?)");
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
            Map<Integer, Set<Role>> roleMap = new HashMap<>();
            jdbcTemplate.query("SELECT * FROM user_roles", rs -> {
                int userId = rs.getInt(1);
                Role role = Role.valueOf(rs.getString(2));
                roleMap.computeIfAbsent(userId, roleList -> new HashSet<>()).add(role);
            });
            users.forEach(user -> user.setRoles(roleMap.get(user.getId())));
        }
        return users;
    }

    private Set<Role> getRoles(User user) {
        Set<Role> roleSet = new HashSet<>();
        jdbcTemplate.query("SELECT role FROM user_roles ur WHERE ur.user_id=?", rs -> {
            roleSet.add(Role.valueOf(rs.getString(1)));
        }, user.getId());
        return roleSet;
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
