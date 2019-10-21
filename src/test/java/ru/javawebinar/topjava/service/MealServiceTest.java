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

import java.time.LocalDateTime;
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
        Meal meal = mealService.get(m3.getId(),USER_ID);
        assertMatch(meal,m3);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFound() {
        mealService.get(m3.getId(),ADMIN_ID);
    }

    @Test
    public void delete() {
        mealService.delete(m2.getId(),USER_ID);
        assertMatch(mealService.getAll(USER_ID),m6,m5,m4,m3,m1);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() {
        mealService.delete(m3.getId(),ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List list = mealService.getBetweenDates(startDate, finishDate,USER_ID);
        assertMatch(list,m6,m5,m4);
    }

    @Test
    public void getAll() {
        List all = mealService.getAll(USER_ID);
        assertMatch(all,m6,m5,m4,m3,m2,m1);
    }

    @Test
    public void update() {
        Meal updated = new Meal(m6);
        updated.setCalories(999);
        updated.setDescription("what");
        mealService.update(updated,USER_ID);
        assertMatch(mealService.get(m6.getId(),USER_ID),updated);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFound() {
        mealService.update(m3,ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.parse("2019-10-19T09:00:00"),"eat more",2222);
        Meal created = mealService.create(newMeal,USER_ID);
        newMeal.setId(created.getId());
        assertMatch(mealService.getAll(USER_ID),newMeal,m6,m5,m4,m3,m2,m1);
    }
}