DROP DATABASE IF EXISTS ifarm;
CREATE DATABASE ifarm;

USE ifarm;

CREATE TABLE farm
(
    `id`      VARCHAR(255) PRIMARY KEY,
    `name`    VARCHAR(255) NOT NULL,
    `address` VARCHAR(255) NOT NULL
);

CREATE TABLE plant
(
    `id`        VARCHAR(255) PRIMARY KEY,
    `name`      VARCHAR(255) NOT NULL,
    `unit_type` VARCHAR(10)  NOT NULL
);

CREATE TABLE farm_plant
(
    `farm_id`  VARCHAR(255) NOT NULL,
    `plant_id` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`farm_id`) REFERENCES farm (`id`),
    FOREIGN KEY (`plant_id`) REFERENCES plant (`id`)
);

CREATE TABLE fertilizer
(
    `id`        VARCHAR(255) PRIMARY KEY,
    `name`      VARCHAR(255) NOT NULL,
    `unit_type` VARCHAR(10)  NOT NULL
);

CREATE TABLE farm_fertilizer
(
    `farm_id`       VARCHAR(255) NOT NULL,
    `fertilizer_id` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`farm_id`) REFERENCES farm (`id`),
    FOREIGN KEY (`fertilizer_id`) REFERENCES fertilizer (`id`)
);

CREATE TABLE pesticide
(
    `id`        VARCHAR(255) PRIMARY KEY,
    `name`      VARCHAR(255) NOT NULL,
    `unit_type` VARCHAR(10)  NOT NULL
);

CREATE TABLE farm_pesticide
(
    `farm_id`      VARCHAR(255) NOT NULL,
    `pesticide_id` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`farm_id`) REFERENCES farm (`id`),
    FOREIGN KEY (`pesticide_id`) REFERENCES pesticide (`id`)
);

CREATE TABLE user
(
    `id`           VARCHAR(255) PRIMARY KEY,
    `name`         VARCHAR(255) NOT NULL,
    `email`        VARCHAR(255) NOT NULL,
    `password`     VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(255) NOT NULL
);

CREATE TABLE user_farm
(
    `user_id` VARCHAR(255) NOT NULL,
    `farm_id` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`user_id`) REFERENCES user (`id`),
    FOREIGN KEY (`farm_id`) REFERENCES farm (`id`)
);

CREATE TABLE activity
(
    `id`       VARCHAR(255) PRIMARY KEY,
    `farm_id`  VARCHAR(255)     NOT NULL,
    `user_id`  VARCHAR(255)     NOT NULL,
    `date`     VARCHAR(255)     NOT NULL,
    `action`   VARCHAR(50)      NOT NULL,
    `type`     VARCHAR(255)     NOT NULL,
    `unit`     VARCHAR(255)     NOT NULL,
    `quantity` DOUBLE PRECISION NOT NULL,
    `field`    INTEGER UNSIGNED NOT NULL,
    `row`      INTEGER UNSIGNED NOT NULL,
    FOREIGN KEY (`farm_id`) REFERENCES farm (`id`),
    FOREIGN KEY (`user_id`) REFERENCES user (`id`)
);
