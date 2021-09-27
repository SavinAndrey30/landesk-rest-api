# ticket-tracker
Репозиторий содержит учебный проект, который был написан с целью тренировки создания REST API с нуля и с целью поработать с новыми для меня технологиями (ActiveMQ, Akka, Caffeine)  
Это REST API автоматизации службы поддержки для обработки инцидентов пользователей. В системе присутствуют пользователи с разными правами доступа (admin, analyst, user) и, соответственно, разным функционалом.  

## Функционал:
- Создание инцидента (POST ``` /api/incidents ```);  
- Просмотр списка инцидентов (GET ``` /api/incidents ```);  
- Редактирование инцидента (PUT ``` /api/incidents ```);  
- Назначение категории инциденту (PATCH ``` /api/incidents/{incidentId}/categories/{categoryId} ```);  
- Назначения статуса инциденту (PATCH ``` /api/incidents/{incidentId}/statuses ```);  
- Назначение аналитика инциденту (PATCH ``` /api/incidents/{incidentId}/analysts/{analystId} ```);  
- Назначение приоритета инциденту (PATCH ``` /api/incidents/priorities/{priorityId} ```);  
- Удаление инцидента (DELETE ``` /api/incidents/{incidentId} ```);  

Также реализованы базовые crud-операции по определенным эндпоинтам для связанных с инцидентами сущностей (категории, отделы, приоритеты и тп), а также операции, доступные только админу, такие как создание/удаление пользователя, создание роли, назначение пользователю роли      

## Stack
Spring Boot, Spring Data JPA, Spring Security (OAUTH 2.0), JUnit, H2, ActiveMQ (для логгирования и отправки сообщений на почту новому пользователю), Akka Actors (для логгирования кол-ва инцидентов), Liquibase, Caffeine Cache, Docker, Maven

## Инструкции по запуску приложения
- Должна быть установлена Java 11+
- Инициализация БД:
1. установить docker клиент
2. создать jar-file командой ``` mvn clean package ```
3. развернуть образы (приложения и ActiveMQ) ``` docker-compose up ``` из корня проекта

Приложение будет доступно по адресу ``` http://localhost:8888 ```

## Тестирование работы API (Postman)  
В Postman на адрес ``` http://localhost:8888/oauth/token ``` отправить POST запрос с авторизацией basic auth (username "landesk", password "secret") и телом x-www-form-urlencoded со следующими данными:  
grant_type: password  
username: выбрать из списка ниже  
password: pass123  

Будет получен access_token и refresh token. Меняю тип авторизации в Postman на Bearer Token и прописываю токен в Headers. Теперь можем получать данные по доступным эндпоинтам.  

- Данные для входа:  
analyst: alena (пароль pass123)  
user: boris (пароль pass123)  
admin: andrey (пароль pass123)

## Модульное тестирование 
В проекте реализовано модульное тестирование сервисного слоя.  
Команда для запуска тестов: ``` mvn clean test ``` или ``` mvnw clean test ```
