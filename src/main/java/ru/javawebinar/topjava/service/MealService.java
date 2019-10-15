package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private MealRepository repository;

    @Autowired
    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, Integer userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, Integer userId) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public Meal get(int id, Integer userId) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public List<Meal> getAllFiltered(LocalDate startDate, LocalDate endDate, Integer userId) {
        return repository.getAllFiltered(startDate, endDate, userId);
    }

    public List<Meal> getAll(Integer userId){
        return repository.getAll(userId);
    }

    public void update(Meal meal, Integer userId) throws NotFoundException {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}
