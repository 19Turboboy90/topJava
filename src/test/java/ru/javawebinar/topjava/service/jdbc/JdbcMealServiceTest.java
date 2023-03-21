package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import javax.validation.ConstraintViolationException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.meal1;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
    @Test
    public void saveNotValid() {
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, null, "duplicate", 100), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), " ", 100), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), null, 100), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), "d", 100), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), "duplicate".repeat(20), 100), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), "duplicate", 0), USER_ID));
        assertThrows(ConstraintViolationException.class, () -> service.create(new Meal(null, meal1.getDateTime(), "duplicate", 6000), USER_ID));
    }
}