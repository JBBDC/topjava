package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.annotations.UniqueUserEmail;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.to.UserTo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUniqueEmailValidator extends EmailValidator implements ConstraintValidator<UniqueUserEmail,  HasId> {

    public UserUniqueEmailValidator(UserService userRepository) {
        super(userRepository);
    }

    public void initialize(UniqueUserEmail constraint) {
    }

    public boolean isValid(HasId user, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("User with this email already exists")
                .addPropertyNode("email")
                .addConstraintViolation();
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
            log.info("{} was thrown in isExistAlready method", e.getMessage());
        }
        return false;
    }
}
