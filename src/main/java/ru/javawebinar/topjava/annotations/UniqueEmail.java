package ru.javawebinar.topjava.annotations;

import ru.javawebinar.topjava.util.UniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {

        String USER_WITH_THIS_EMAIL_ALREADY_EXISTS = "User with this email already exists";

        String message() default USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
