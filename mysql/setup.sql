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