package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal expected = mealService.get(USER_MEAL3.getId(), USER_ID);
        assertMatch(expected, USER_MEAL3);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        mealService.get(USER_MEAL3.getId(), ADMIN_ID);
    }

    @Test
    public void delete() {
        mealService.delete(USER_MEAL2.getId(), USER_ID);
        assertMatch(mealService.getAll(USER_ID), USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        mealService.delete(USER_MEAL3.getId(), ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List list = mealService.getBetweenDates(startDate, finishDate, USER_ID);
        assertMatch(list, USER_MEAL6, USER_MEAL5, USER_MEAL4);
    }

    @Test
    public void getAll() {
        List all = mealService.getAll(USER_ID);
        assertMatch(all, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(USER_MEAL6);
        updated.setCalories(999);
        updated.setDescription("what");
        mealService.update(updated, USER_ID);
        assertMatch(mealService.get(USER_MEAL6.getId(), USER_ID), updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        mealService.update(USER_MEAL3, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.of(LocalDate.of(2019, 10, 19),
                LocalTime.of(9, 0, 0)), "eat more", 2222);
        Meal created = mealService.create(newMeal, USER_ID);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(USER_ID), newMeal, USER_MEAL6, USER_MEAL5, USER_MEAL4, USER_MEAL3, USER_MEAL2, USER_MEAL1);
        assertMatch(newMeal, created);
    }
}