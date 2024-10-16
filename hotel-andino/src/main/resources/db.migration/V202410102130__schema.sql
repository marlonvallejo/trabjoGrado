CREATE SCHEMA IF NOT EXISTS `hotel_andino_db`;
USE `hotel_andino_db`;

-- -----------------------------------------------------
-- Table `hotel_andino_db`.`hibernate_sequence`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`hibernate_sequence`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`hibernate_sequence`
(
    `next_val` BIGINT NOT NULL
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Insert `hotel_andino_db`.`hibernate_sequence`
-- -----------------------------------------------------
INSERT INTO `hotel_andino_db`.`hibernate_sequence`(next_val) VALUES (1);

-- -----------------------------------------------------
-- Table `hotel_andino_db`.`rol_usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`rol_usuario`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`rol_usuario`
(
    `id`         BIGINT      NOT NULL AUTO_INCREMENT,
    `nombre_rol` VARCHAR(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
    )
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hotel_andino_db`.`usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`usuario`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`usuario`
(
    `id`             BIGINT       NOT NULL AUTO_INCREMENT,
    `nombre`         VARCHAR(64)  DEFAULT NULL,
    `apellido`       VARCHAR(64)  DEFAULT NULL,
    `nombre_usuario` VARCHAR(45)  NOT NULL UNIQUE,
    `correo`         VARCHAR(255) DEFAULT NULL,
    `contrasenia`    VARCHAR(255) NOT NULL,
    `es_activo`      TINYINT(1)   DEFAULT NULL,
    PRIMARY KEY (`id`)
    )
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hotel_andino_db`.`usuario_rol_usuario`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`usuario_rol_usuario`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`usuario_rol_usuario`
(
    `usuario_id`     BIGINT NOT NULL,
    `rol_usuario_id` BIGINT NOT NULL,
    PRIMARY KEY (`usuario_id`, `rol_usuario_id`),
    KEY `fk_usuario_rol_usuario_has_usuario` (`usuario_id`),
    KEY `fk_usuario_rol_usuario_has_rol_usuario` (`rol_usuario_id`),
    CONSTRAINT `fk_usuario_rol_usuario_has_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
    CONSTRAINT `fk_usuario_rol_usuario_has_rol_usuario` FOREIGN KEY (`rol_usuario_id`) REFERENCES `rol_usuario` (`id`)
    )
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hotel_andino_db`.`habitacion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`usuario_rol_usuario`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`usuario_rol_usuario`
(
    `id`     BIGINT NOT NULL,
    `numero` BIGINT NOT NULL,
    `tipo`   BIGINT NOT NULL,
    `precio` VARCHAR(45) DEFAULT NULL,
    `estado` VARCHAR(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
    )

    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;

-- -----------------------------------------------------
-- Table `hotel_andino_db`.`reserva`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `hotel_andino_db`.`reserva`;
CREATE TABLE IF NOT EXISTS `hotel_andino_db`.`reserva`
(
    `id`           BIGINT      NOT NULL AUTO_INCREMENT,
    `fecha_ingreso` DATETIME(6) NOT NULL,
    `fecha_salida`  DATETIME(6) NOT NULL,
    `usuario_id`    BIGINT      NOT NULL,
    `habitacion_id` BIGINT      NOT NULL,
    `precio_total`        VARCHAR(45) DEFAULT NULL,

    PRIMARY KEY (`id`),
    KEY `fk_reserva_has_usuario` (`usuario_id`),
    KEY `fk_reserva_has_habitacion` (`habitacion_id`),
    CONSTRAINT `fk_reserva_has_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`),
    CONSTRAINT `fk_reserva_has_habitacion` FOREIGN KEY (`habitacion_id`) REFERENCES `habitacion` (`id`)
    )
    ENGINE = InnoDB
    AUTO_INCREMENT = 1
    DEFAULT CHARSET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;
