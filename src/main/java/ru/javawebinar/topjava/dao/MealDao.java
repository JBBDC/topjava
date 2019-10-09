package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
     Meal create(Meal meal);

     void delete(Long id);

     Meal update(Meal meal);

     Meal getById(Long id);

     List<Meal> getAll();
}
