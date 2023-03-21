package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final LocalDate DATA_THRESHOLD = LocalDate.of(1895, 12, 28);

    @Override
    public void validateFields(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATA_THRESHOLD)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidateException("Указанная дата фильма раньше, чем 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new ValidateException("Указанная продолжительность фильма меньше или равна нулю");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() < 1) {
            log.warn("Описание фильма: {}", film.getDescription());
            throw new ValidateException("Несоответствующее количество символов в описании фильма");
        }

    }

    @GetMapping
    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        validateFields(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {}, успешно добавлен", film.getName());
        return film;
    }

    @PutMapping
    @Override
    public Film put(@Valid @RequestBody Film film) {
        validateFields(film);
        if(!films.containsKey(film.getId())) {
            throw new ValidateException("Обновление данных о фильме невозможно - такого фильма нет в базе");
        }
        films.put(film.getId(), film);
        log.info("Успешное обновление информации о фильме: {}",film.getName());
        return film;
    }
}
