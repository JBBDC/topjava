package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.annotations.UniqueUserEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static ru.javawebinar.topjava.annotations.UniqueEmail.USER_WITH_THIS_EMAIL_ALREADY_EXISTS;

public class UserUniqueEmailValidator extends Validator implements ConstraintValidator<UniqueUserEmail,  HasId> {

    UserService service;

    public UserUniqueEmailValidator(UserService service) {
        this.service = service;
    }

    @Override
    public void initialize(UniqueUserEmail constraint) {
    }
    @Override
    public boolean isValid(HasId user, ConstraintValidatorContext context) {
        setCustomConstraintViolation(context,USER_WITH_THIS_EMAIL_ALREADY_EXISTS,"email");
        return user != null && !isExistAlready(user);
    }

    boolean isExistAlready(HasId user) {
        String email = "";
        if(user instanceof UserTo) {
            email = ((UserTo) user).getEmail();
        }
        if(user instanceof User) {
            email = ((User) user).getEmail();
        }
        try {
            User existed = service.getByEmail(email);
            if (!user.isNew() && user.getId().equals(existed.getId())) {
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
