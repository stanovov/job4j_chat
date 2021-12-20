[![Build Status](https://app.travis-ci.com/stanovov/job4j_chat.svg?branch=master)](https://app.travis-ci.com/stanovov/job4j_chat)

![](https://img.shields.io/badge/Maven-=_3-red)
![](https://img.shields.io/badge/Java-=_14-orange)
![](https://img.shields.io/badge/Spring-=_5-darkorange)
![](https://img.shields.io/badge/Liquibase-=_3-f02a18)
![](https://img.shields.io/badge/PostgerSQL-=_9-blue)
![](https://img.shields.io/badge/Checkstyle-lightgrey)

# job4j_chat

+ [О проекте](#О-проекте)
+ [Технологии](#Технологии)
+ [Использование](#Использование)
+ [Контакты](#Контакты)

## О проекте

RESTful веб-приложение для чата с комнатами. Имеются модели: пользователь, роль, комната и сообщение. Пользователю будет 
назначена роль. Пользователь может находиться сразу в нескольких комнатах чата. Пользователь может отправлять сообщения. 
В системе используется авторизация по токену. При регистрации пользователю выдается токен и с использованием этого 
токена пользователь может отправлять запросы в систему.

## Технологии

+ **Java 14**, **Spring (Boot, Data JPA, Web, Security (JWT))**;
+ **PostgreSQL**, **Liquibase**;
+ Сборщик проектов **Maven**;
+ Непрерывная интеграция - **Travis CI**;
+ Инструмент для анализа стиля кода - **Checkstyle**;

## Использование

### Комнаты

`GET /rooms/` - получить все комнаты

`GET /rooms/{id}` - получить комнату по её id

`POST /rooms/` - создать комнату

`PUT /rooms/` - обновить данные комнаты или создать новую

`PATCH /rooms/` - обновить данные существующей комнаты

`DELETE /rooms/{id}` - удалить комнату по её id

### Роли

`GET /roles/` - получить все роли

`GET /roles/{id}` - получить роль по её id

`POST /roles/` - создать роль

`PUT /roles/` - обновить данные роли или создать новую

`PATCH /roles/` - обновить данные существующей роли

`DELETE /roles/{id}` - удалить роль по её id

### Пользователи

`GET /users/` - получить всех пользователей

`GET /users/{id}` - получить пользователя по его id

`GET /users/{id}/role/` - получить роль пользователя по id пользователя

`GET /users/{id}/rooms/` - получить список комнат, в которых состоит пользователь по id пользователя

`POST /users/sign-up` - создать пользователя

`POST /login` - получить токен пользователя (в теле передает username и password созданного пользователя)

`PUT /users/` - обновить данные пользователя или создать нового

`PATCH /users/` - обновить данные существующего пользователя

`PUT /users/{id}/role/` - обновить роль пользователя по id пользователя

`PUT /users/{id}/rooms/` - добавить комнату пользователю (зайти в комнату) по id пользователя

`DELETE /users/{id}` - удалить пользователя по его id

`DELETE /users/{id}/role` - удалить роль пользователя по id пользователя

`DELETE /users/{id}/rooms/{roomId}` - удалить комнату пользователя (выйти из комнаты) по id пользователя и id комнаты

### Сообщения

`GET /messages/` - получить все сообщения

`GET /messages/{id}` - получить сообщение по его id

`POST /messages/` - создать сообщение

`PUT /messages/` - обновить данные сообщения или создать новое

`PATCH /messages/` - обновить данные существующего сообщения

`DELETE /messages/{id}` - удалить сообщение по его id

## Контакты

Становов Семён Сергеевич

Email: sestanovov@gmail.com

Telegram: [@stanovovss](https://t.me/stanovovss)