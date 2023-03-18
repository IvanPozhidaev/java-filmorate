package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate DATA_THRESHOLD = LocalDate.of(1895, 12, 28);

    private void validateFilmFields(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATA_THRESHOLD)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("Указанная дата фильма раньше, чем 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Указанная продолжительность фильма меньше или равна нулю");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() < 1) {
            log.warn("Описание фильма: {}", film.getDescription());
            throw new ValidationException("Несоответствующее количество символов в описании фильма");
        }

    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateFilmFields(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {}, успешно добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validateFilmFields(film);
        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Обновление данных о фильме невозможно - такого фильма нет в базе");
        }
        films.put(film.getId(), film);
        log.info("Успешное обновление информации о фильме: {}",film.getName());
        return film;
    }
}
