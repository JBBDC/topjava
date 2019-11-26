package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class StringToLocalTimeConverter implements Converter<String, LocalTime> {
    @Override
    public LocalTime convert(String source) {
        if(!StringUtils.isEmpty(source)) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(source, formatter);
        } else {
            return null;
        }
    }
}
