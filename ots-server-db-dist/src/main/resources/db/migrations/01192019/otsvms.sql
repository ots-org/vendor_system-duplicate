-- MySQL Workbench Synchronization
-- Generated: 2019-01-20 00:49
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: SERAJKU

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

CREATE SCHEMA IF NOT EXISTS `otsvms` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE IF NOT EXISTS `otsvms`.`ots_registration` (
  `ots_registration_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_registration_firstname` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_lastname` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_addr1` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_addr2` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_pincode` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_emailid` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_profile_pic` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_registration_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_users_mapped_to` INT(11) NOT NULL,
  `ots_user_role_id` INT(11) NOT NULL,
  PRIMARY KEY (`ots_registration_id`),
  INDEX `fk_ots_registration_ots_users1_idx` (`ots_users_mapped_to` ASC) ,
  INDEX `fk_ots_registration_ots_user_role1_idx` (`ots_user_role_id` ASC) ,
  CONSTRAINT `fk_ots_registration_ots_users1`
    FOREIGN KEY (`ots_users_mapped_to`)
    REFERENCES `otsvms`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_registration_ots_user_role1`
    FOREIGN KEY (`ots_user_role_id`)
    REFERENCES `otsvms`.`ots_user_role` (`ots_user_role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms`.`ots_users` (
  `ots_users_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_users_firstname` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_lastname` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_addr1` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_addr2` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_pincode` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_emailid` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_profile_pic` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_users_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_registration_id` INT(11) NOT NULL,
  `ots_user_role_id` INT(11) NOT NULL,
  PRIMARY KEY (`ots_users_id`),
  INDEX `fk_ots_users_ots_registration_idx` (`ots_registration_id` ASC) ,
  INDEX `fk_ots_users_ots_user_role1_idx` (`ots_user_role_id` ASC) ,
  CONSTRAINT `fk_ots_users_ots_registration`
    FOREIGN KEY (`ots_registration_id`)
    REFERENCES `otsvms`.`ots_registration` (`ots_registration_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_users_ots_user_role1`
    FOREIGN KEY (`ots_user_role_id`)
    REFERENCES `otsvms`.`ots_user_role` (`ots_user_role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms`.`ots_user_role` (
  `ots_user_role_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_user_role_code` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_name` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_user_role_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_user_role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms`.`ots_user_mapping` (
  `ots_user_mapping_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_mapped_to` INT(11) NULL DEFAULT NULL,
  `ots_users_id` INT(11) NOT NULL,
  `ots_user_mapping_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_user_mapping_created` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`ots_user_mapping_id`),
  INDEX `fk_ots_user_mapping_ots_users1_idx` (`ots_users_id` ASC) ,
  CONSTRAINT `fk_ots_user_mapping_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
