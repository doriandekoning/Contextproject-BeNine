-- MySQL Script generated by MySQL Workbench
-- 06/14/16 17:22:09
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema presetsdatabase
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema presetsdatabase
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `presetsdatabase` ;
USE `presetsdatabase` ;

-- -----------------------------------------------------
-- Table `presetsdatabase`.`camera`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`camera` (
  `ID` INT(11) NOT NULL,
  `MACaddress` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`preset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`preset` (
  `ID` INT(11) NOT NULL,
  `Image` CHAR(50) NULL DEFAULT '',
  `camera_ID` INT(11) NOT NULL,
  `name` CHAR(50) NULL DEFAULT '',
  PRIMARY KEY (`ID`),
  INDEX `fk_simplepreset_camera_idx` (`camera_ID` ASC),
  CONSTRAINT `fk_simplepreset_camera`
    FOREIGN KEY (`camera_ID`)
    REFERENCES `presetsdatabase`.`camera` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`IPpreset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`IPpreset` (
  `Pan` DOUBLE NULL DEFAULT NULL,
  `Tilt` DOUBLE NULL DEFAULT NULL,
  `Zoom` DOUBLE NULL DEFAULT NULL,
  `Focus` DOUBLE NULL DEFAULT NULL,
  `Iris` DOUBLE NULL DEFAULT NULL,
  `Autofocus` INT(11) NULL DEFAULT NULL,
  `Panspeed` DOUBLE NULL DEFAULT NULL,
  `Tiltspeed` DOUBLE NULL DEFAULT NULL,
  `Autoiris` INT(11) NULL DEFAULT NULL,
  `preset_ID` INT(11) NOT NULL,
  PRIMARY KEY (`preset_ID`),
  CONSTRAINT `fk_IPpreset_preset1`
    FOREIGN KEY (`preset_ID`)
    REFERENCES `presetsdatabase`.`preset` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`tag` (
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`tagPreset`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`tagPreset` (
  `tag_name` VARCHAR(45) NOT NULL,
  `preset_ID` INT(11) NOT NULL,
  PRIMARY KEY (`tag_name`, `preset_ID`),
  INDEX `fk_tagPreset_tag1_idx` (`tag_name` ASC),
  INDEX `fk_tagPreset_preset1_idx` (`preset_ID` ASC),
  CONSTRAINT `fk_tagPreset_tag1`
    FOREIGN KEY (`tag_name`)
    REFERENCES `presetsdatabase`.`tag` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tagPreset_preset1`
    FOREIGN KEY (`preset_ID`)
    REFERENCES `presetsdatabase`.`preset` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`queue`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`queue` (
  `ID` INT(11) NOT NULL,
  `Name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `presetsdatabase`.`presetsList`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsdatabase`.`presetsList` (
  `Sequence` INT(11) NOT NULL,
  `queue_ID` INT(11) NOT NULL,
  `preset_ID` INT(11) NOT NULL,
  PRIMARY KEY (`queue_ID`, `Sequence`, `preset_ID`),
  INDEX `fk_presetsList_preset1_idx` (`preset_ID` ASC),
  CONSTRAINT `fk_presetsList_queue1`
    FOREIGN KEY (`queue_ID`)
    REFERENCES `presetsdatabase`.`queue` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_presetsList_preset1`
    FOREIGN KEY (`preset_ID`)
    REFERENCES `presetsdatabase`.`preset` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
