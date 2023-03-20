package ru.yandex.practicum.filmorate.controller;

import java.util.Collection;

public abstract class Controller <T> {

    public abstract Collection<T> getAll();

    public abstract void validateFields(T e);

    public abstract T create(T e);

    public abstract T put(T e);
}
