package ru.javawebinar.topjava.service.impl;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.impl.MockMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

public class MealServiceImpl implements MealService {
    private MealDao mealDao = new MockMealDao();

    @Override
    public void create(Meal meal) {
        mealDao.create(meal);
    }

    @Override
    public void delete(Long id) {
        mealDao.delete(id);
    }

    @Override
    public void update(Meal meal) {
        mealDao.update(meal);
    }

    @Override
    public Meal getById(Long id) {
        return mealDao.getById(id);
    }

    @Override
    public List<Meal> getAll() {
        return mealDao.getAll();
    }
}
