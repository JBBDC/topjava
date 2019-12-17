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
        String message() default "User with this email already exists";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
