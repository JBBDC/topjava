package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.annotations.UniqueDateTime;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueDateTimeValidator implements ConstraintValidator<UniqueDateTime, Meal> {

    final Logger log = LoggerFactory.getLogger(UniqueDateTimeValidator.class);

    @Autowired
    MealService service;

    public UniqueDateTimeValidator() {}

    @Override
    public void initialize(UniqueDateTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Meal meal, ConstraintValidatorContext context) {
        return meal != null && !isExistAlready(meal);
    }

    boolean isExistAlready(Meal meal) {
        try {
            Meal existed = service.getByDateTime(meal.getDateTime(), SecurityUtil.authUserId());
            boolean isDateExist = existed.getDateTime().equals(meal.getDateTime());
            if( isDateExist && !meal.isNew()) {
                return !existed.getId().equals(meal.getId());
            }
            return isDateExist;
        } catch (Exception e) {
            log.info("{} was thrown in isExistAlready method", e.getMessage());
        }
        return false;
    }
}
