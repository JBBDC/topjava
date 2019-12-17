package ru.javawebinar.topjava.annotations;

import ru.javawebinar.topjava.util.UniqueDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueDateTimeValidator.class)
public @interface UniqueDateTime {

    String MEAL_WITH_THIS_DATETIME_ALREADY_EXISTS = "Meal with this DateTime already exists";

    String message() default MEAL_WITH_THIS_DATETIME_ALREADY_EXISTS;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
