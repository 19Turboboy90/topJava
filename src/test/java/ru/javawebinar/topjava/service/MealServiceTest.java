package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestDate.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        assertMatch(service.get(MEAL_ID1, USER_ID), meal1);
    }

    @Test
    public void getNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_EXIST_ID, USER_ID));
    }

    @Test
    public void getSomeoneElseMeal() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID1, ADMIN_ID));
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID1, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL_ID1, USER_ID));
    }

    @Test
    public void deleteNotFoundMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_EXIST_ID, ADMIN_ID));
    }

    @Test
    public void deleteSomeoneElseMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL_ID1, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(LocalDate.of(2020, 1, 30),
                LocalDate.of(2020, 1, 30), USER_ID);
        assertMatch(meals, meal4, meal3, meal2, meal1);
    }

    @Test
    public void filteringBoundariesAreNotSet() {
        List<Meal> meals = service.getBetweenInclusive(null, LocalDate.of(2020, 1, 30), USER_ID);
        assertMatch(meals, meal4, meal3, meal2, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> mealList = service.getAll(USER_ID);
        assertMatch(mealList, meal7, meal6, meal5, meal4, meal3, meal2, meal1);
    }

    @Test
    public void update() {
        Meal update = getUpdated();
        service.update(update, USER_ID);
        assertMatch(service.get(update.getId(), USER_ID), getUpdated());
    }

    @Test
    public void updateSomeoneElseMeal() {
        Meal updateMeal = service.create(getNew(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.update(updateMeal, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        Meal newMeal = getNew();
        newMeal.setId(created.getId());

        assertMatch(created, newMeal);
        assertMatch(service.get(created.getId(), USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        Meal duplicateMeal = new Meal(null, meal1.getDateTime(), "DuplicateMeal", 500);
        assertThrows(DuplicateKeyException.class, () -> service.create(duplicateMeal, USER_ID));
    }
}