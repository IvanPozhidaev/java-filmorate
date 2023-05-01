# Filmorate
Filmorate - проект по созданию сервиса рекомендации фильмов на основе оценок пользователей

## База данных

### ER-диаграма
![ER diagram](src/main/resources/schema_db.png)

### Пояснения к схеме

Простые первичные ключи в схеме выделены жирным начертанием.

Связи в данной таблице: один к одному (1--1), один ко многим (1--*).

### Примеры запросов

Добавление новой записи в таблицу films:
```sql
INSERT INTO films (title, description, release, duration, mpa_id)
VALUES ('Название фильма', 'Описание фильма', '2023-05-01', 120, 1);
```

Поиск фильма по его идентификатору:
```sql
SELECT *
FROM films 
WHERE film_id = 1;
```

Поиск идентификаторов пользователей, которые поставили фильму лайк:
```sql
SELECT user_id
FROM likes
WHERE film_id = 1;
```

Получение данных пользователя по его идентификатору
```sql
SELECT *
FROM users
WHERE id = 1;
```

Поиск друзей пользователя по его идентификатору
```sql
SELECT friend_id
FROM friends
WHERE user_id = 1
UNION
SELECT user_id
FROM friends
WHERE friend_id = 2
AND friendship_confirmed = TRUE;
```