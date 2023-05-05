package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Создан пользователь с id: {}", user.getId());
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        userService.setUserNameByLogin(user, "обновлён");
        log.info("Пользователь с id обновлён: {}", user.getId());
        return userService.updateUser(user);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("GET-запрос на получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id) {
        log.info("Получен пользователь с id: '{}'", id);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public User deleteById(@PathVariable Integer id) {
        return userService.deleteById(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable String id, @PathVariable String friendId) {
        userService.addFriend(id, friendId);
        log.info("Обновлён пользователь id: {} и добавлен друг с id: {}", id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable String id, @PathVariable String friendId) {
        userService.deleteFriend(id, friendId);
        log.info("Обновлён пользователь id: {} и удалён друг с id: {}", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable Integer id) {
        log.info("GET-запрос на получение списка друзей");
        return userService.getUserFriends(String.valueOf(id));
    }

    @GetMapping("/{id}/friends/common/{anotherId}")
    public Collection<User> getCommonFriendsList(@PathVariable String id, @PathVariable String anotherId) {
        log.info("GET-запрос на получение списка общих друзей");
        return userService.getCommonFriendsList(id, anotherId);
    }
}