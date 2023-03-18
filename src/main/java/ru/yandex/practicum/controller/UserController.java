package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    private void validateUserFields(@Valid @RequestBody User user) {
        if(user.getLogin().contains(" ")) {
            log.warn("Логин пользователя: {}", user.getLogin());
            throw new ValidationException("Логин пользователя не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if(user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Возраст: {}", user.getBirthday());
            throw new ValidationException("Указанная дата рождения находится в будущем");
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
        log.info("Добавлен пользователь. Логин: {}, email: {}", user.getLogin(), user.getEmail());
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        validateUserFields(user);
        if(!users.containsKey(user.getId())) {
            throw new ValidationException("Обновление невозможно - пользователя не существует");
        }
        users.put(user.getId(), user);
        log.info("Обновление данных пользователя с id: {} и логином: {}", user.getId(), user.getLogin());
        return user;
    }

}
