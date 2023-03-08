package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {
    @Test
    public void getUserByIdWithMeals() {
        User user = service.getUserByIdWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(user.getMeals(), MealTestData.meals);
    }

    @Test
    public void getUserByIdWithoutMeals() {
        User guest = service.getUserByIdWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(guest, UserTestData.guest);
        MEAL_MATCHER.assertMatch(guest.getMeals(), UserTestData.guest.getMeals());
    }
}
