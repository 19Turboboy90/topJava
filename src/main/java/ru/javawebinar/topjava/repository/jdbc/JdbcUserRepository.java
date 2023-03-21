package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertUser;
    private final Validator validator;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate, Validator validator) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("users").usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.validator = validator;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException("Save user - validation errors", violations);
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            saveUserRoles(user, user.id());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        } else {
            jdbcTemplate.update("DELETE FROM user_role WHERE user_id = ?", user.id());
            saveUserRoles(user, user.id());
        }
        return user;
    }

    private void saveUserRoles(User user, int newId) {
        Set<Role> roles = user.getRoles();
        if (!roles.isEmpty()) {
            jdbcTemplate.batchUpdate("INSERT INTO user_role (user_id, role) VALUES (?,?)", roles, roles.size(), (ps, role) -> {
                ps.setInt(1, newId);
                ps.setString(2, role.name());
            });
        }
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return queryRolesForUser(jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id));
    }

    private User queryRolesForUser(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user == null) {
            return null;
        }
        List<String> roles = jdbcTemplate.queryForList("SELECT role FROM user_role WHERE user_id=?", String.class, user.id());
        user.setRoles(roles.stream().map(Role::valueOf).toList());
        return user;
    }

    @Override
    public User getByEmail(String email) {
        return queryRolesForUser(jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email));
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT us.*, ur.role FROM users AS us "
                + "LEFT JOIN user_role AS ur ON us.id = ur.user_id "
                + "ORDER BY name, email", new UserWithRolesRowMapper());
        return users.stream().distinct().toList();
    }

    private static class UserWithRolesRowMapper implements RowMapper<User> {

        private final Map<Integer, User> usersMap = new HashMap<>();

        @Override
        public User mapRow(@Nullable ResultSet rs, int rowNum) throws SQLException {
            Objects.requireNonNull(rs);
            User user = Objects.requireNonNull(ROW_MAPPER.mapRow(rs, rowNum));

            String roleValue = rs.getString("role");
            if (roleValue != null && !roleValue.isEmpty()) {
                user.setRoles(List.of(Role.valueOf(roleValue)));
            } else {
                user.setRoles(List.of());
            }

            user = usersMap.merge(user.id(), user, (oldUser, newUser) -> {
                Role role = newUser.getRoles().iterator().next();
                if (role != null) {
                    oldUser.getRoles().add(role);
                }
                return oldUser;
            });

            return user;
        }
    }
}
