package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user);

    User getUserById(Integer id);

    User deleteUserById(Integer id);

    boolean deleteUser(User user);

    Collection<User> getAllUsers();

    boolean addFriend(Integer firstId, Integer secondId);

    boolean deleteFriend(Integer userId, Integer friendId);

    Map<Integer, User> getUsers();

    User getUser(final Integer id);
}
