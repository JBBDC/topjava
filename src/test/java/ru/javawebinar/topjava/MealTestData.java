package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final LocalDate startDate = LocalDate.of(2019, 10, 18);
    public static final LocalDate finishDate = LocalDate.of(2019, 10, 18);

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final int ID = START_SEQ + 2;

    public static final Meal USER_MEAL1 = new Meal(ID, LocalDateTime.of(LocalDate.of(2019, 10, 17),
            LocalTime.of(9, 0)), "breakfast", 500);
    public static final Meal USER_MEAL2 = new Meal(ID + 1, LocalDateTime.of(LocalDate.of(2019, 10, 17),
            LocalTime.of(13, 0, 0)), "lunch", 1000);
    public static final Meal USER_MEAL3 = new Meal(ID + 2, LocalDateTime.of(LocalDate.of(2019, 10, 17),
            LocalTime.of(18, 0, 0)), "dinner", 1000);
    public static final Meal USER_MEAL4 = new Meal(ID + 3, LocalDateTime.of(LocalDate.of(2019, 10, 18),
            LocalTime.of(10, 0, 0)), "first", 700);
    public static final Meal USER_MEAL5 = new Meal(ID + 4, LocalDateTime.of(LocalDate.of(2019, 10, 18),
            LocalTime.of(14, 0, 0)), "second", 600);
    public static final Meal USER_MEAL6 = new Meal(ID + 5, LocalDateTime.of(LocalDate.of(2019, 10, 18),
            LocalTime.of(22, 0, 0)), "third", 500);

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
