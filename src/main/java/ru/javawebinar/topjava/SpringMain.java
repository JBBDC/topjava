package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.time.LocalDateTime;
import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            MealRestController mealRestController = appCtx.getBean(MealRestController.class);

            mealRestController.getAll().forEach(System.out::println);
            mealRestController.delete(4);
            mealRestController.getAll().forEach(System.out::println);
            SecurityUtil.setAuthUserId(2);
            mealRestController.create(new Meal(LocalDateTime.now(), "whatever", 300));
            mealRestController.getAll().forEach(System.out::println);
            mealRestController.update(new Meal(7, LocalDateTime.now(), "changeIt", 400), 7);
            mealRestController.getAll().forEach(System.out::println);
            System.out.println(mealRestController.get(7));
//            System.out.println(mealRestController.get(93));
            mealRestController.update(new Meal(5, LocalDateTime.now(), "changeItToo", 666), 7);
            mealRestController.getAll().forEach(System.out::println);
        }
    }
}
