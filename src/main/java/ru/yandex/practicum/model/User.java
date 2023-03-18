package ru.yandex.practicum.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString
@EqualsAndHashCode
public class User {
    private int id;

    @NotNull
    @Email
    private String email;

    @NotBlank
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;
}
