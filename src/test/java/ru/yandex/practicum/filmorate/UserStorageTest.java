package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)

public class UserStorageTest {
    private final UserDbStorage userStorage;

    private User user1 = new User(1,
            "test1@test.ru",
            "login1",
            "Name1",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());
    private User user2 = new User(2,
            "test2@test.ru",
            "login2",
            "Name2",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());
    private User user3 = new User(3,
            "test3@test.ru",
            "login3",
            "Name3",
            LocalDate.of(1980, 1, 1),
            new ArrayList<>());

    @Test
    void createUserTest() {
        userStorage.createUser(user1);
        AssertionsForClassTypes.assertThat(user1).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user1).extracting("name").isNotNull();
    }

    @Test
    void getAllUsersTest() {
        userStorage.createUser(user2);
        userStorage.createUser(user3);
        Collection<User> dbUsers = userStorage.getAllUsers();
        assertEquals(2, dbUsers.size());
    }

    @Test
    void deleteUserTest() {
        Collection<User> beforeDelete = userStorage.getAllUsers();
        userStorage.deleteUser(user1);
        Collection<User> afterDelete = userStorage.getAllUsers();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}