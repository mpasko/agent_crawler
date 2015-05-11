-- MySQL Script generated by MySQL Workbench
-- 05/09/15 11:02:42
-- Model: New Model    Version: 1.0
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`Osoba`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Osoba` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Osoba` (
  `id` INT NOT NULL,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Przyjaciel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Przyjaciel` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Przyjaciel` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Przyjaciel_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_Przyjaciel_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `mydb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Miejsce`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Miejsce` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Miejsce` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  `typ` ENUM('szkola', 'praca','mieszkanie') NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Miejsce_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_Miejsce_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `mydb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`CoLubi`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`CoLubi` ;

CREATE TABLE IF NOT EXISTS `mydb`.`CoLubi` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_CoLubi_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_CoLubi_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `mydb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Interakcja`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`Interakcja` ;

CREATE TABLE IF NOT EXISTS `mydb`.`Interakcja` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `z_kim` VARCHAR(90) NULL,
  `czas` DATETIME NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Interakcja_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_Interakcja_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `mydb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
