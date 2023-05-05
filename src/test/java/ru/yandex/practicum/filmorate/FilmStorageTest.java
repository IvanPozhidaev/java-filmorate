package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmDbStorage filmStorage;

    private final Film film1 = new Film(1,
            "film1 name",
            "film1 description",
            LocalDate.now().minusYears(10),
            90,
            7,
            new Mpa(1, "name", "description"),
            new ArrayList<>(),
            new ArrayList<>());

    private final Film film2 = new Film(2,
            "film2 name",
            "film2 description",
            LocalDate.now().minusYears(15),
            120,
            2,
            new Mpa(3, "name", "description"),
            new ArrayList<>(),
            new ArrayList<>());

    private final Film film = new Film(1,
            "Name film",
            "Description film",
            LocalDate.now().minusYears(10),
            100,
            7,
            new Mpa(1, "Name", "Description"),
            new ArrayList<>(),
            new ArrayList<>());

    @Test
    public void addFilmTest() {
        filmStorage.createFilm(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    public void getFilmByIdTest() {
        filmStorage.createFilm(film);
        Film dbFilm = filmStorage.getFilmById(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    public void updateFilmTest() {
        Film added = filmStorage.createFilm(film);
        added.setName("film updated");
        filmStorage.updateFilm(added);
        Film dbFilm = filmStorage.getFilmById(added.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "film updated");
    }

    @Test
    public void deleteFilmTest() {
        Film addedFilm1 = filmStorage.createFilm(film1);
        Film addedFilm2 = filmStorage.createFilm(film2);
        Collection<Film> beforeDelete = filmStorage.getAllFilms();
        filmStorage.deleteFilm(addedFilm1);
        Collection<Film> afterDelete = filmStorage.getAllFilms();
        assertEquals(beforeDelete.size() - 1, afterDelete.size());
    }
}
