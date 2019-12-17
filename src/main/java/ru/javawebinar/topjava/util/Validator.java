package ru.javawebinar.topjava.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidatorContext;

public abstract class Validator {

    public Validator() {
    }

    final Logger log = LoggerFactory.getLogger(UniqueEmailValidator.class);

    public void logException(Throwable e){
        log.info("{} was thrown", e.getMessage());
    }

    public void setCustomConstraintViolation(ConstraintValidatorContext context, String template, String property) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(template)
                .addPropertyNode(property)
                .addConstraintViolation();
    }
}
