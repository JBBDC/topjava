package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final LocalDate startDate = LocalDate.parse("2019-10-18");
    public static final LocalDate finishDate = LocalDate.parse("2019-10-18");

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int ID = START_SEQ + 2;

    public static final Meal m1 = new Meal(ID, LocalDateTime.parse("2019-10-17T09:00:00"),
            "breakfast",500);
    public static final Meal m2 = new Meal(ID + 1, LocalDateTime.parse("2019-10-17T13:00:00"),
            "lunch",1000);
    public static final Meal m3 = new Meal(ID + 2, LocalDateTime.parse("2019-10-17T18:00:00"),
            "dinner", 1000);
    public static final Meal m4 = new Meal(ID + 3, LocalDateTime.parse("2019-10-18T10:00:00"),
            "first",700);
    public static final Meal m5 = new Meal(ID + 4, LocalDateTime.parse("2019-10-18T14:00:00"),
            "second",600);
    public static final Meal m6 = new Meal(ID + 5, LocalDateTime.parse("2019-10-18T22:00:00"),
            "third",500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
