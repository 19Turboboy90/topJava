package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetweenHalfOpen;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
        MealsUtil.meals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {} userId={}", meal, userId);
        Map<Integer, Meal> repositoryMeal = repository.computeIfAbsent(userId, unused -> new ConcurrentHashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repositoryMeal.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return repositoryMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {} userId={}", id, userId);
        Map<Integer, Meal> repositoryMeal = repository.get(userId);
        return repositoryMeal.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {} userId={}", id, userId);
        Map<Integer, Meal> repositoryMeal = repository.get(userId);
        return repositoryMeal.get(id) != null ? repositoryMeal.get(id) : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll userId={}", userId);
        return getAllFiltered(userId, null, null);
    }

    @Override
    public List<Meal> getAllFiltered(int userId, LocalDate start, LocalDate finish) {
        log.info("getAllFiltered");
        Map<Integer, Meal> repositoryMeal = repository.get(userId);
        return repositoryMeal == null
                ? Collections.emptyList()
                : repositoryMeal.values().stream()
                .filter(meal -> isBetweenHalfOpen(meal.getDate(), start, finish))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}


