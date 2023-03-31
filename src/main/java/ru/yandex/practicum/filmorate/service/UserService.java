package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        log.info("Список пользователей отправлен");
        return userStorage.findAll();
    }

    public User getById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с id {} отправлен", id);
        return userStorage.getById(id);
    }

    public User deleteById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден - удаление невозможно");
        }
        log.info("Пользователь с id {} удалён", id);
        return userStorage.deleteById(id);
    }

    public List<User> addFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException("По данным id один или несколько пользователей не найдены");
        }
        if (userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи уже являются друзьями");
        }
        userStorage.getById(firstId).getFriends().add(secondId);
        userStorage.getById(secondId).getFriends().add(firstId);
        log.info("Пользователи {} и {} теперь являются друзьями",
                userStorage.getById(firstId).getName(),
                userStorage.getById(secondId).getName());
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }

    public List<User> deleteFriend(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException("По данным id один или несколько пользователей не найдены");
        }
        if (!userStorage.getById(firstId).getFriends().contains(secondId)) {
            throw new InternalException("Пользователи не являются друзьями");
        }
        userStorage.getById(firstId).getFriends().remove(secondId);
        userStorage.getById(secondId).getFriends().remove(firstId);
        log.info("Пользователи {} и {} теперь не являются друзьями",
                userStorage.getById(firstId).getName(),
                userStorage.getById(secondId).getName());
        return Arrays.asList(userStorage.getById(firstId), userStorage.getById(secondId));
    }

    public List<User> getFriendsListById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден - невозможно получить список друзей");
        }
        log.info("Список друзей пользователя {} получен", userStorage.getById(id).getName());
        return userStorage.getById(id).getFriends().stream()
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }

    public List<User> getMutualFriendsList(int firstId, int secondId) {
        if (!userStorage.getUsers().containsKey(firstId) || !userStorage.getUsers().containsKey(secondId)) {
            throw new ObjectNotFoundException("По данным id один или несколько пользователей не найдены");
        }
        log.info("Список общих друзей пользователей {} и {} получен",
                userStorage.getById(firstId).getName(),
                userStorage.getById(secondId).getName());
        return userStorage.getById(firstId).getFriends().stream()
                .filter(friendId -> userStorage.getById(secondId).getFriends().contains(friendId))
                .map(userStorage::getById)
                .collect(Collectors.toList());
    }
}
