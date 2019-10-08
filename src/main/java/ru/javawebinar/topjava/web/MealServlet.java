package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(UserServlet.class);

    private MealService mealService = MealsUtil.service;

    private Long editRowId = null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        List<MealTo> allMeals = MealsUtil.getFiltered(mealService.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        if (editRowId != null) {
            request.setAttribute("editRow", editRowId);
            editRowId = null;
        }
        request.setAttribute("meals", allMeals);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        switch (req.getParameter("action")) {
            case "create":
                try {
                    LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
                    String description = req.getParameter("description");
                    int calories = Integer.parseInt(req.getParameter("calories"));
                    mealService.create(new Meal(dateTime, description, calories));
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                break;
            case "delete":
                Long id = Long.parseLong(req.getParameter("id"));
                mealService.delete(id);
                break;
            case "edit":
                editRowId = Long.parseLong(req.getParameter("id"));
            case "save":
                try {
                    Long mealId = Long.parseLong(req.getParameter("id"));
                    LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
                    String description = req.getParameter("description");
                    int calories = Integer.parseInt(req.getParameter("calories"));
                    mealService.update(new Meal(mealId, dateTime, description, calories));
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                break;
        }
        resp.sendRedirect("meals");
    }
}
