-- MySQL Script generated by MySQL Workbench
-- 05/20/16 16:47:07
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
-- -----------------------------------------------------
-- Schema presetsDatabase
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema presetsDatabase
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `presetsDatabase` ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `presetsDatabase`.`camera`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsDatabase`.`camera` (
  `ID` INT(11) NOT NULL,
  `MACaddress` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `presetsDatabase`.`presets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsDatabase`.`presets` (
  `ID` INT(11) NOT NULL,
  `Pan` INT(11) NULL DEFAULT NULL,
  `Tilt` INT(11) NULL DEFAULT NULL,
  `Zoom` INT(11) NULL DEFAULT NULL,
  `Focus` INT(11) NULL DEFAULT NULL,
  `Iris` INT(11) NULL DEFAULT NULL,
  `Autofocus` INT(11) NULL DEFAULT NULL,
  `Panspeed` INT(11) NULL DEFAULT NULL,
  `Tiltspeed` INT(11) NULL DEFAULT NULL,
  `Autoiris` INT(11) NULL DEFAULT NULL,
  `Image` CHAR(50) NULL DEFAULT '',
  `camera_ID` INT(11) NOT NULL,
  PRIMARY KEY (`ID`, `camera_ID`),
  INDEX `fk_presets_camera_idx` (`camera_ID` ASC),
  CONSTRAINT `fk_presets_camera`
    FOREIGN KEY (`camera_ID`)
    REFERENCES `presetsDatabase`.`camera` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `presetsDatabase`.`keyword`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `presetsDatabase`.`keyword` (
  `ID` INT NOT NULL,
  `Name` VARCHAR(45) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`keywordPresets`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`keywordPresets` (
  `presets_ID` INT(11) NOT NULL,
  `keyword_ID` INT NOT NULL,
  PRIMARY KEY (`presets_ID`, `keyword_ID`),
  INDEX `fk_keywordPresets_keyword1_idx` (`keyword_ID` ASC),
  CONSTRAINT `fk_keywordPresets_presets`
    FOREIGN KEY (`presets_ID`)
    REFERENCES `presetsDatabase`.`presets` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_keywordPresets_keyword1`
    FOREIGN KEY (`keyword_ID`)
    REFERENCES `presetsDatabase`.`keyword` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

USE `presetsDatabase` ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
