package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MockMealDao;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


public class MealServlet extends HttpServlet {

    private MealDao mealDao = new MockMealDao();

    private AtomicLong editRowId = new AtomicLong();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        List<MealTo> allMeals = MealsUtil.getFiltered(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        if (editRowId != null) {
            request.setAttribute("editRow", editRowId);
            editRowId = new AtomicLong();
        }
        request.setAttribute("meals", allMeals);
        request.setAttribute("formatter", formatter);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        switch (req.getParameter("action")) {
            case "create":
                LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
                String description = req.getParameter("description");
                int calories = Integer.parseInt(req.getParameter("calories"));
                mealDao.create(new Meal(dateTime, description, calories));
                break;
            case "delete":
                Long id = Long.parseLong(req.getParameter("id"));
                mealDao.delete(id);
                break;
            case "edit":
                editRowId.set(Long.parseLong(req.getParameter("id")));
                break;
            case "save":
                Long mealId = Long.parseLong(req.getParameter("id"));
                dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
                description = req.getParameter("description");
                calories = Integer.parseInt(req.getParameter("calories"));
                mealDao.update(new Meal(mealId, dateTime, description, calories));
                break;
        }
        resp.sendRedirect("meals");
    }
}
