package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;
    private static final LocalDate DATE_THRESHOLD = LocalDate.of(1895, 12, 28);

    private void validateFilmFields(@Valid @RequestBody Film film) {
        if(film.getReleaseDate().isBefore(DATE_THRESHOLD)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidationException("Указанная дата релиза раньше 28 декабря 1895 года");
        }
        if(film.getDuration() <= 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidationException("Указанная продолжительность фильма меньше или равна 0");
        }
        if(film.getDescription().length() > 201) {
            log.warn("Длтна описания фильма: {}", film.getDescription());
            throw new ValidationException("Количество символов в описании фильма превышает 200");
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
        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        validateFilmFields(film);
        if(!films.containsKey(film.getId())) {
            throw new ValidationException("Обновление невозможно - фильма нет в базе");
        }
        films.put(film.getId(), film);
        log.info("Обновление данных о фильме с id: {} и названием: {}", film.getId(), film.getName());
        return film;
    }
}
