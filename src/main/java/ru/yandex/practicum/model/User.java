package ru.yandex.practicum.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotNull
    @Email
    private final String email;

    @NotNull
    @NotBlank
    private final String login;
    private String name;

    @NotNull
    private final LocalDate birthday;
}
