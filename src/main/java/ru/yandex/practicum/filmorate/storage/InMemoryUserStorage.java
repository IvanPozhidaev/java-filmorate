package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    void validateFields(User user) {
        if (user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            log.warn("Логин пользователя: {}", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы или быть пустым");
        }
        if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
            log.warn("Имя пользователя было не заполнено или было пустое - заменено на логин {}", user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Возраст пользователя: {}", user.getBirthday());
            throw new ValidationException("Дата рождения пользователя указана в будущем");
        }
        if (user.getEmail().isBlank() || user.getEmail() == null || user.getEmail().equals(" ")) {
            log.warn("Email пользователя: {}", user.getEmail());
            throw new ValidationException("Поле email не должно быть пустым");
        }
    }


    @Override
    public User create(User user) {
        validateFields(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Пользователь с логином: {}, успешно добавлен", user.getLogin());
        return user;
    }


    @Override
    public User update(User user) {
        validateFields(user);
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("Обновление данных о пользователе невозможно, т.к. его нет в базе");
        }
        users.put(user.getId(), user);
        log.info("Успешное обновление информации о пользователе с id: {}, и логином {}", user.getId(), user.getLogin());
        return user;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public Map<Integer, User> getUsers() {
        return users;
    }

    @Override
    public User getById(int id) {
        return users.get(id);
    }

    @Override
    public User deleteById(int id) {
        User user = users.get(id);
        users.remove(id);
        return user;
    }


}
