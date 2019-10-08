package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {
     void create(Meal meal);

     void delete(Long id);

     void update(Meal meal);

     Meal getById(Long id);

     List<Meal> getAll();
}
