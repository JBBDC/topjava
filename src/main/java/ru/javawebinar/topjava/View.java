package ru.javawebinar.topjava;

import javax.validation.groups.Default;

public class View {
    public interface Persist extends Default {}

    public interface onProfileUpdate extends Default {}

    public interface onCreate extends Default {}
}