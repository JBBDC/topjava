package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try {
            appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
            controller = appCtx.getBean(MealRestController.class);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id)
                ,LocalDateTime.parse(request.getParameter("dateTime"))
                ,request.getParameter("description")
                ,Integer.parseInt(request.getParameter("calories")));

        log.info(id.isEmpty() ? "Create {}" : "Update {}", id);
        if (id.isEmpty()) controller.create(meal);
        else controller.update(meal, Integer.parseInt(id));
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                if (request.getParameter("startDate") == null || request.getParameter("endDate") == null
                        || request.getParameter("startTime") == null || request.getParameter("endTime") == null) {
                    request.setAttribute("meals", controller.getAll());
                } else {
                    request.setAttribute("meals", controller.getAllFiltered(
                            request.getParameter("startDate").isEmpty() ? LocalDate.MIN
                                    : LocalDate.parse(request.getParameter("startDate")),
                            request.getParameter("endDate").isEmpty() ? LocalDate.MAX
                                    : LocalDate.parse(request.getParameter("endDate")),
                            request.getParameter("startTime").isEmpty() ? LocalTime.MIN
                                    : LocalTime.parse(request.getParameter("startTime")),
                            request.getParameter("endTime").isEmpty() ? LocalTime.MAX
                                    : LocalTime.parse(request.getParameter("endTime"))
                    ));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
