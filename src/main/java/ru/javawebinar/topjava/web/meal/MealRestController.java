package ru.javawebinar.topjava.web.meal;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.StringToLocalDateConverter;
import ru.javawebinar.topjava.util.StringToLocalTimeConverter;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        ConversionService conversionService = binder.getConversionService();
        ConverterRegistry registry = (ConverterRegistry) conversionService;
        registry.addConverter(new StringToLocalTimeConverter());
        registry.addConverter(new StringToLocalDateConverter());

    }

    static final String REST_URL = "/rest/meals";

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Meal get(@PathVariable int id) {
        return super.get(id);
    }

    @GetMapping
    @ResponseBody
    public List<MealTo> getAll() {
        return super.getAll();
    }

    @GetMapping(value = "/filter")
    @ResponseBody
    public List<MealTo> getBetween(@RequestParam @Nullable LocalDate startDate,
                                   @RequestParam @Nullable LocalDate endDate,
                                   @RequestParam @Nullable LocalTime startTime,
                                   @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal) {
        Meal created = super.create(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Override
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal, @PathVariable int id) {
        super.update(meal, id);
    }
}