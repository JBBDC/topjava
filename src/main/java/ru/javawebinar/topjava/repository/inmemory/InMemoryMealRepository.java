package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, HashMap<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return getUserMeals(userId).put(meal.getId(), meal);
        }
        // treat case: update, but not present in storage
        HashMap<Integer, Meal> map = repository.get(userId);
        if (map != null && map.get(meal.getId()) != null) {
            return map.put(meal.getId(), meal);
        }
        return null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
            return getUserMeals(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
            return getUserMeals(userId).get(id);
    }

    @Override
    public List<Meal> getAllFiltered(LocalDate startDate, LocalDate endDate, Integer userId) {
                    return getUserMeals(userId).values().stream()
                    .filter(meal -> DateTimeUtil.isBetween(meal.getDate(), startDate, endDate))
                    .sorted(Comparator.comparing(Meal::getDate).reversed())
                    .collect(Collectors.toList());
            }

    @Override
    public List<Meal> getAll(Integer userId) {
        return new ArrayList<>(repository.computeIfAbsent(userId, map -> new HashMap<>()).values());
    }

    private HashMap<Integer, Meal> getUserMeals(int userId) {
        return repository.computeIfAbsent(userId, map -> new HashMap<>());
    }

}

