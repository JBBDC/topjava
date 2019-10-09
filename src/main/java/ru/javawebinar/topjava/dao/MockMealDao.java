package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicLong;

public class MockMealDao implements MealDao {

    private Set<Meal> meals = new ConcurrentSkipListSet<>();

    private AtomicLong idCounter = new AtomicLong(meals.size() + 1);

    public MockMealDao() {
        create(new Meal(1L, LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        create(new Meal(2L, LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        create(new Meal(3L, LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        create(new Meal(4L, LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        create(new Meal(5L, LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        create(new Meal(6L, LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public synchronized Meal create(Meal meal) {
        if (meal != null) {
            meal.setId(createID().longValue());
            meals.add(meal);
        }
        return meal;
    }

    public synchronized Meal update(Meal meal) {
        if (meal != null) {
            meals.removeIf(m -> m.getId().equals(meal.getId()));
            meals.add(meal);
        }
        return meal;
    }

    public synchronized void delete(Long id) {
        meals.removeIf(m -> m.getId().equals(id));
    }

    public List<Meal> getAll() {
        return new ArrayList<>(meals);
    }

    public synchronized Meal getById(Long id) {
        return meals.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
    }

    private AtomicLong createID() {
        return new AtomicLong(idCounter.getAndIncrement());
    }
}
