package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    private int id;
    private Set<Integer> usersLikes = new HashSet<>();

    @NotBlank
    private String name;

    @NotBlank
    @Size(max = 200, message = "Лимит описания - 200 символов")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Min(value = 1)
    private int duration;
}
