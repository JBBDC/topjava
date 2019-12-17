package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.annotations.UniqueEmail;
import ru.javawebinar.topjava.service.UserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator extends EmailValidator implements ConstraintValidator<UniqueEmail, String> {


    public UniqueEmailValidator(UserService userRepository) {
        super(userRepository);
    }

    public void initialize(UniqueEmail constraint) {
    }

    public boolean isValid(String user, ConstraintValidatorContext context) {
        return user != null && !isExistAlready(user);
    }

}
