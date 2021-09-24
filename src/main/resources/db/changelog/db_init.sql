DROP TABLE IF EXISTS users_roles cascade;
DROP TABLE IF EXISTS roles cascade;
DROP TABLE IF EXISTS incident cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS status cascade;
DROP TABLE IF EXISTS priority cascade;
DROP TABLE IF EXISTS department cascade;
DROP TABLE IF EXISTS category cascade;
DROP TABLE IF EXISTS oauth_client_details;
DROP TABLE IF EXISTS oauth_access_token;
DROP TABLE IF EXISTS oauth_refresh_token;

CREATE TABLE users
(
    id           bigint       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username     varchar(50)  NOT NULL,
    password     varchar(255) NOT NULL,
    userFullName varchar(50) DEFAULT NULL,
    userLocation varchar(50) DEFAULT NULL,
    userEmail    varchar(50)  NOT NULL,
    userPhone    varchar(50) DEFAULT NULL
);

INSERT INTO users (username, password, userFullName, userLocation, userEmail, userPhone)
VALUES ('andrey', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Savin AP', 'Moscow',
        'andrey@gmail.com', '+79232222222'),
       ('boris', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Borisov BD', 'Moscow',
        'boris@gmail.com', '+79232222222'),
       ('alena', '$2a$10$/K.WdZlnm6GJni3i2ahWiueIovcoUoTHMHIDYD.JzqDL7RcMG8Q.y', 'Alena HD', 'Kazan',
        'alena@gmail.com', '+79232222222');


CREATE TABLE roles
(
    id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) DEFAULT NULL
);
INSERT INTO roles (name)
VALUES ('ROLE_USER'),
       ('ROLE_ANALYST'),
       ('ROLE_ADMIN');

Create table users_roles
(
    user_id BIGINT NOT NULL,
    role_id INT    NOT NULL,
    CONSTRAINT FK_USER FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_ROLE FOREIGN KEY (role_id)
        REFERENCES roles (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 1),
       (3, 1),
       (3, 2);


CREATE TABLE category
(
    id          INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL
);

INSERT INTO category (name, description)
VALUES ('ПО', 'проблемы с существующим ПО'),
       ('Аппаратная проблема', 'Оборудование вышло из строя'),
       ('Подключение рабочего места', 'Подключение рабочего места для сотрудников');

CREATE TABLE priority
(
    id   INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL
);
INSERT INTO priority (name)
VALUES 'Высокий', 'Низкий', 'Средний';

CREATE TABLE department
(
    id          INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255) DEFAULT NULL
);
INSERT INTO department (name, description)
VALUES ('ОТК', 'Отдел технического контроля'),
       ('НИЦ', 'Научно-исследовательский отдел'),
       ('ОПП', 'Отдел производственного планирования');

CREATE TABLE incident
(
    id                 BIGINT       NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name               VARCHAR(50)  NOT NULL,
    screenshotFileName VARCHAR(255) DEFAULT NULL,
    errorDescription   VARCHAR(1000) DEFAULT NULL,
    dateCreated        TIMESTAMP     DEFAULT NULL,
    deadline           TIMESTAMP     DEFAULT NULL,
    dateClosed         TIMESTAMP     DEFAULT NULL,
    department_id      INT           DEFAULT NULL,
    initiatorFullName  VARCHAR(50)   DEFAULT NULL,
    initiatorUsername  VARCHAR(50)  NOT NULL,
    initiatorPhone     VARCHAR(50)   DEFAULT NULL,
    initiatorLocation  VARCHAR(50)   DEFAULT NULL,
    initiatorEmail     VARCHAR(50)  NOT NULL,
    analyst_id         BIGINT        DEFAULT NULL,
    priority_id        INT          NOT NULL,
    category_id        INT          NOT NULL,
    CONSTRAINT FK_DEPARTMENT FOREIGN KEY (department_id)
        REFERENCES department (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_ANALYST FOREIGN KEY (analyst_id)
        REFERENCES users (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_PRIORITY FOREIGN KEY (priority_id)
        REFERENCES priority (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT FK_CATEGORY FOREIGN KEY (category_id)
        REFERENCES category (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO incident (name, screenshotFileName, errorDescription, dateCreated, deadline, dateClosed, department_id,
                      initiatorFullName, initiatorUsername, initiatorPhone, initiatorLocation, initiatorEmail,
                      analyst_id, priority_id, category_id)
VALUES ('ошибка в программе', 'screenshot1.jpg', 'Вылетает ошибка при запуске программы', '2021-08-13 18:28:52.876968', '2021-08-16 18:28:52.876968', null, '1',
        'Borisov BD', 'boris', '+79232222222', 'Moscow', 'boris@landesk.com', 3, 1, 1),
       ('ошибка в программе', 'screenshot2.jpg', 'Не работает функционал', '2021-08-13 18:28:52.876968', '2021-08-16 18:28:52.876968', null, '1', 'Borisov BD', 'boris',
        '+79232222222', 'Moscow', 'boris@landesk.com', 3, 1, 1),
       ('Скайп ошибка', 'screenshot3.jpg', 'Ошибка при запуске skype', '2021-08-13 18:28:52.876968', '2021-08-16 18:28:52.876968', null, '2', 'Borisov BD', 'boris',
        '+79232222222', 'Moscow', 'boris@landesk.com', 1, 2, 1);

CREATE TABLE status
(
    id                           BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name                         VARCHAR(50) NOT NULL,
    dateStatusAssignedToIncident TIMESTAMP DEFAULT NULL,
    incident_id                  BIGINT    DEFAULT NULL,
    CONSTRAINT FK_INCIDENT FOREIGN KEY (incident_id)
        REFERENCES incident (id)
        ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO status (name, dateStatusAssignedToIncident, incident_id)
VALUES ('открыт', '2021-08-13 18:28:52.876968', 1),
       ('открыт', '2021-08-13 18:28:52.876968', 2),
       ('открыт', '2021-08-13 18:28:52.876968', 3),
       ('закрыт', '2021-08-13 18:28:52.876968', 3),
       ('ожидание информации', '2021-08-13 18:28:52.876968', 1);


CREATE TABLE IF NOT EXISTS oauth_client_details
(
    client_id               VARCHAR(255) PRIMARY KEY,
    resource_ids            VARCHAR(255),
    client_secret           VARCHAR(255),
    scope                   VARCHAR(255),
    authorized_grant_types  VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities             VARCHAR(255),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    autoapprove             VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS oauth_access_token
(
    token_id          VARCHAR(255),
    token             BYTEA,
    authentication_id VARCHAR(255) PRIMARY KEY,
    user_name         VARCHAR(255),
    client_id         VARCHAR(255),
    authentication    BYTEA,
    refresh_token     VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS oauth_refresh_token
(
    token_id       VARCHAR(255),
    token          BYTEA,
    authentication BYTEA
    );

INSERT INTO oauth_client_details (client_id, resource_ids, client_secret, scope, authorized_grant_types,
                                  web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity,
                                  additional_information, autoapprove)
VALUES ('landesk', 'api', '$2a$10$8j5ufdVBdusoTC40ZR35BOtoIjbC1qX4pK2fPDyX2Ymd1.g2wAHcC', 'read,write',
        'password,authorization_code,refresh_token', NULL, NULL, 86400, 86400, 'Client', NULL);
