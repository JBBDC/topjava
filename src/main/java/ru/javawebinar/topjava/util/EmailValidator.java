package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.SecurityUtil;

public abstract class EmailValidator {

    final Logger log = LoggerFactory.getLogger(UniqueEmailValidator.class);

    UserService service;

    public EmailValidator(UserService userRepository) {
        this.service = userRepository;
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
            log.info("{} was thrown in isExistAlready method", e.getMessage());
        }
        return false;
    }
}
