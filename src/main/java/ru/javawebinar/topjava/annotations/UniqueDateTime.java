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
    String message() default "Meal with this DateTime already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
