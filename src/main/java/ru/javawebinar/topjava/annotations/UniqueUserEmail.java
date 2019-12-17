package ru.javawebinar.topjava.annotations;

import ru.javawebinar.topjava.util.UserUniqueEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserUniqueEmailValidator.class)
public @interface UniqueUserEmail {
        String message() default "User with this email already exists";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};

}
