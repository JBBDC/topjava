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

import static ru.javawebinar.topjava.annotations.UniqueDateTime.MEAL_WITH_THIS_DATETIME_ALREADY_EXISTS;

public class UniqueDateTimeValidator extends Validator implements ConstraintValidator<UniqueDateTime, Meal> {

    final Logger log = LoggerFactory.getLogger(UniqueDateTimeValidator.class);

    @Autowired
    MealService service;

    public UniqueDateTimeValidator() {}

    @Override
    public void initialize(UniqueDateTime constraintAnnotation) {
    }

    @Override
    public boolean isValid(Meal meal, ConstraintValidatorContext context) {
        setCustomConstraintViolation(context,MEAL_WITH_THIS_DATETIME_ALREADY_EXISTS,"dateTime");
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
            logException(e);
        }
        return false;
    }
}
