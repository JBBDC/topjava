package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping(value = "/meals", method = RequestMethod.GET)
public class JspMealController extends AbstractMealController {

    @Autowired
    private MealService service;

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("")
    public String getAll(HttpServletRequest request, Model model) {
        int userId = authUserId();
        if (isFiltered(request)) {
            LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
            LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
            LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
            LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
            request.setAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        } else {
            List<MealTo> meals = MealsUtil.getTos(service.getAll(userId), authUserCaloriesPerDay());
            model.addAttribute("meals", meals);
        }
        log.info("get all meals for user {}", userId);
        return "meals";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        int userId = authUserId();
        int id = getId(request);
        service.delete(id, userId);
        log.info("delete meal {} for user {}", id, authUserId());
        return "redirect:" + getBaseUrl(request);
    }

    @GetMapping("/create")
    public String create(HttpServletRequest request, Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update")
    public String update(HttpServletRequest request, Model model) {
        model.addAttribute("meal", service.get(getId(request), SecurityUtil.authUserId()));
        return "mealForm";
    }

    @PostMapping("/mealForm")
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (StringUtils.isEmpty(request.getParameter("id"))) {
            service.create(meal, SecurityUtil.authUserId());
            log.info("create meal for user {}", authUserId());
        } else {
            ValidationUtil.assureIdConsistent(meal, getId(request));
            service.update(meal, SecurityUtil.authUserId());
            log.info("update meal {} for user {}", meal.getId(), authUserId());
        }
        return "redirect:" + getBaseUrl(request);
    }

    private boolean isFiltered(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        List filterParams = List.of("startDate", "endDate", "startTime", "endTime");
        return params.entrySet().stream()
                .anyMatch(e -> filterParams.contains(e.getKey()) && !StringUtils.isEmpty(e.getValue()));
    }

    private int getId(HttpServletRequest request) {
        String id = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(id);
    }

    private static String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme() + "://";
        String serverName = request.getServerName();
        String serverPort = (request.getServerPort() == 80) ? "" : ":" + request.getServerPort();
        String contextPath = request.getContextPath();
        return scheme + serverName + serverPort + contextPath + "/meals";
    }

}
