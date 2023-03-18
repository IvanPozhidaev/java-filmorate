package ru.yandex.practicum.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {
    private int id;

    @NotNull
    @NotBlank
    private final String name;
    private final String description;

    @NotNull
    private final LocalDate releaseDate;

    @NotNull
    private final int duration;
}
