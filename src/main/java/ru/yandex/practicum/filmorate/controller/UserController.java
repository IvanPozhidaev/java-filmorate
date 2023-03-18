package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private void validateUserFields(@Valid @RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин пользователя: {}", user.getLogin());
            throw new ValidateException("Логин не может содержать пробелы");
        }
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Возраст пользователя: {}", user.getBirthday());
            throw new ValidateException("Дата рождения пользователя указана в будущем");
        }
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        validateUserFields(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь с логином: {}, успешно добавлен", user.getLogin());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validateUserFields(user);
        if(!users.containsKey(user.getId())) {
            throw new ValidateException("Обновление данных о пользователе невозможно, т.к. его нет в базе");
        }
        users.put(user.getId(), user);
        log.info("Успешное обновление информации о пользователе с id: {}, и логином {}", user.getId(), user.getLogin());
        return user;
    }
}
