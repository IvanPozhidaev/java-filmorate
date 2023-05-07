package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private int counter = 1;
    private final Validator validator;
    private final UserStorage userStorage;

    @Autowired
    public UserService(Validator validator, UserStorage userStorage) {
        this.validator = validator;
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.info("Список пользователей");
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        validate(user);
        log.info("Пользователь создан");
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validate(user);
        log.info("Пользователь обновлён");
        return userStorage.updateUser(user);
    }

    public User getUserById(Integer id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        log.info("Пользователь с id: '{}' отправлен", id);
        return userStorage.getUserById(id);
    }

    public User deleteById(int id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь не найден. Невозможно удалить неизветсного пользователя");
        }
        log.info("Пользователь с id: '{}' удален", id);
        return userStorage.deleteUserById(id);
    }

    public void addFriend(final String userId, final String friendId) {
        User user = getUserStored(userId);
        User friend = getUserStored(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
        log.info("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", userId, friendId);
    }

    public void deleteFriend(final String userId, final String friendId) {
        User user = getUserStored(userId);
        User friend = getUserStored(friendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
        log.info("Пользователь с id: '{}' добавлен с список друзей пользователя с id: '{}'", userId, friendId);
    }

    public Collection<User> getUserFriends(String userId) {
        User user = getUserStored(userId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    public User getUserById(final String id) {
        return getUserStored(id);
    }

    private User getUserStored(final String id) {
        final int userId = parseId(id);
        if (userId == Integer.MIN_VALUE) {
            throw new NotFoundException("Не удалось найти id пользователя: %d", id);
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id: '%d' не зарегистрирован!", String.valueOf(userId));
        }
        return user;
    }

    private Integer parseId(final String id) {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    public void setUserNameByLogin(User user, String text) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        log.debug("{} пользователь: '{}', email: '{}'", text, user.getName(), user.getEmail());
    }

    private void validate(final User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.info("Установлено значение {} из поля login для поля name", user.getLogin());
        } else if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Установлено значение {} из поля login для поля name", user.getLogin());
        }
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<User> userConstraintViolation : violations) {
                sb.append(userConstraintViolation.getMessage());
            }
            throw new UserValidationException("Ошибка валидации Пользователя: " + sb, violations);
        }
        if (user.getId() == 0) {
            user.setId(counter++);
        }
    }

    public Collection<User> getCommonFriendsList(final String supposedUserId, final String supposedOtherId) {
        User user = getUserStored(supposedUserId);
        User otherUser = getUserStored(supposedOtherId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }
}