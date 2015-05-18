-- MySQL Script generated by MySQL Workbench
-- 05/17/15 23:37:31
-- Model: New Model    Version: 1.0
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema twitterdb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `twitterdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `twitterdb` ;

-- -----------------------------------------------------
-- Table `twitterdb`.`Osoba`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `twitterdb`.`Osoba` ;

CREATE TABLE IF NOT EXISTS `twitterdb`.`Osoba` (
  `id` INT NOT NULL,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  `miejsce` VARCHAR(90) NULL,
  `celebryta` BOOLEAN NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `twitterdb`.`Przyjaciel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `twitterdb`.`Przyjaciel` ;

CREATE TABLE IF NOT EXISTS `twitterdb`.`Przyjaciel` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(90) NULL,
  `link` VARCHAR(90) NULL,
  `Osoba_id` INT NOT NULL,
  `typ` ENUM('friend', 'follower') NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Przyjaciel_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_Przyjaciel_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `twitterdb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `twitterdb`.`Tweet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `twitterdb`.`Tweet` ;

CREATE TABLE IF NOT EXISTS `twitterdb`.`Tweet` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `link` VARCHAR(90) NULL,
  `czas` DATETIME NULL,
  `typ` ENUM('url', 'user', 'symbol', 'retweet') NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Interakcja_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_Interakcja_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `twitterdb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `twitterdb`.`ListaDyskusyjna`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `twitterdb`.`ListaDyskusyjna` ;

CREATE TABLE IF NOT EXISTS `twitterdb`.`ListaDyskusyjna` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `nazwa` VARCHAR(90) NULL,
  `typ` ENUM('member', 'subscriber') NULL,
  `Osoba_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_ListaDyskusyjna_Osoba1_idx` (`Osoba_id` ASC),
  CONSTRAINT `fk_ListaDyskusyjna_Osoba1`
    FOREIGN KEY (`Osoba_id`)
    REFERENCES `twitterdb`.`Osoba` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
