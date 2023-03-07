package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.MealTestData.MEAL_MATCHER;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = Profiles.DATAJPA)
@Transactional
public class DataJpaUserTest extends AbstractUserServiceTest {
    @Test
    public void getUserByIdWithMeals() {
        User user = service.getUserByIdWithMeals(USER_ID);
        USER_MATCHER.assertMatch(user, getUserWithMeals);
        MEAL_MATCHER.assertMatch(user.getMeals(), getUserWithMeals.getMeals());

        User guest = service.getUserByIdWithMeals(GUEST_ID);
        USER_MATCHER.assertMatch(guest, UserTestData.guest);
        MEAL_MATCHER.assertMatch(guest.getMeals(), UserTestData.guest.getMeals());
    }
}
