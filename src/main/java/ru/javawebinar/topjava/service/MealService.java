package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    void create(Meal meal);

    void delete(Long id);

    void update(Meal meal);

    Meal getById(Long id);

    List<Meal> getAll();
}
