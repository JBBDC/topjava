package ru.javawebinar.topjava.dao.impl;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class MockMealDao implements MealDao {

    private static CopyOnWriteArrayList<Meal> meals = new CopyOnWriteArrayList<>(Arrays.asList(
            new Meal(1L, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(2L, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(3L, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(4L, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(5L, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(6L, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
    ));

    private static AtomicLong idCounter = new AtomicLong(meals.size() + 1);

    public MockMealDao() {
    }

    public void create(Meal meal) {
        if (meal != null) {
            meal.setId(createID().longValue());
            meals.add(meal);
        }
    }

    public void update(Meal meal) {
        if (meal != null) {
            meals.removeIf(m -> m.getId().equals(meal.getId()));
            meals.add(meal);
        }
    }

    public void delete(Long id) {
        meals.removeIf(m -> m.getId().equals(id));
    }

    public List<Meal> getAll() {
        return meals;
    }

    public Meal getById(Long id) {
        return meals.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    private static AtomicLong createID() {
        return new AtomicLong(idCounter.getAndIncrement());
    }
}
