package ru.javawebinar.topjava.util;

import org.springframework.beans.factory.annotation.Autowired;
import ru.javawebinar.topjava.annotations.UniqueEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueEmailValidator extends Validator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    UserService service;

    public UniqueEmailValidator() {
    }

    @Override
    public void initialize(UniqueEmail constraint) {
    }
    @Override
    public boolean isValid(String user, ConstraintValidatorContext context) {
        return user != null && !isExistAlready(user);
    }

    boolean isExistAlready(String email) {
        try {
            User existed = service.getByEmail(email);
            if (existed.getId().equals(SecurityUtil.authUserId())) {
                return false;
            }
            if (existed.getEmail().equals(email)) {
                return true;
            }
        } catch (Exception e) {
            logException(e);
        }
        return false;
    }

}
