package ru.javawebinar.topjava.annotations;

import ru.javawebinar.topjava.util.UserUniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.javawebinar.topjava.annotations.UniqueEmail.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserUniqueEmailValidator.class)
public @interface UniqueUserEmail {
        String message() default USER_WITH_THIS_EMAIL_ALREADY_EXISTS;
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
