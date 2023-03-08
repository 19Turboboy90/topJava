package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.NOT_FOUND;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void getMealByIdWithUser() {
        Meal meal = service.getMealByIdWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, adminMeal1);
        USER_MATCHER.assertMatch(meal.getUser(), admin);
    }

    @Test
    public void getByIdWithUserNotFound() {
        assertThrows(NotFoundException.class, () -> service.getMealByIdWithUser(NOT_FOUND, USER_ID));
    }

    @Test
    public void getByIdWithUserNotOwn() {
        assertThrows(NotFoundException.class, () -> service.getMealByIdWithUser(ADMIN_MEAL_ID, USER_ID));
    }
}
