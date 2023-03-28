package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalException;
import ru.yandex.practicum.filmorate.exception.ValidateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private int id = 1;
    private static final LocalDate DATE_THRESHOLD = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    void validateFields(Film film) {
        if(film.getName().isBlank()) {
            log.warn("Название фильма: {}", film.getName());
            throw new ValidateException("Пустое поле вместо названия фильма недопустимо");
        }
        if (film.getReleaseDate().isBefore(DATE_THRESHOLD)) {
            log.warn("Дата выпуска фильма: {}", film.getReleaseDate());
            throw new ValidateException("Указанная дата фильма раньше, чем 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма: {}", film.getDuration());
            throw new InternalException("Указанная продолжительность фильма меньше или равна нулю");
        }
        if (film.getDescription().length() > 200 || film.getDescription().length() < 1) {
            log.warn("Описание фильма: {}", film.getDescription());
            throw new ValidateException("Несоответствующее количество символов в описании фильма");
        }
    }

    @Override
    public Film create(Film film) {
        validateFields(film);
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Фильм: {}, успешно добавлен", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        validateFields(film);
        if(!films.containsKey(film.getId())) {
            throw new ValidateException("Обновление данных о фильме невозможно - такого фильма нет в базе");
        }
        films.put(film.getId(), film);
        log.info("Успешное обновление информации о фильме: {}",film.getName());
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }

    @Override
    public Film getById(int id) {
        return films.get(id);
    }

    @Override
    public Film deleteById(int id) {
        Film film = films.get(id);
        films.remove(id);
        return film;
    }

    @Override
    public Map<Integer, Film> getFilms() {
        return films;
    }

}
