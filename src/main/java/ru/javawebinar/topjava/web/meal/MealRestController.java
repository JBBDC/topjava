package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;


@Controller
public class MealRestController {
    @Autowired
    private MealService service;
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final int DEFAULT_CALORIES_PER_DAY = 2000;

    public List<MealTo> getAll() {
        log.info("getAll");
        return MealsUtil.getTos(service.getAll(), DEFAULT_CALORIES_PER_DAY);
    }

    public List<MealTo> getAllFiltered(String startDate, String endDate, String startTime, String endTime) {
        log.info("getAllFiltered");
        return new ArrayList<>(MealsUtil.getFilteredTos(
                service.getAll(startDate.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDate),
                        endDate.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDate),
                        SecurityUtil.authUserId()),
                DEFAULT_CALORIES_PER_DAY,
                startTime.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTime),
                endTime.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTime)
        ));
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        return service.create(meal, SecurityUtil.authUserId());
    }

    public Meal get(int id) {
        log.info("get meal with id {}", id);
        return service.get(id, SecurityUtil.authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, SecurityUtil.authUserId());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, SecurityUtil.authUserId());
    }


}