-- MySQL Workbench Synchronization
-- Generated: 2019-03-07 11:54
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: SERAJKU

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

ALTER SCHEMA `otsvms_qa`  DEFAULT CHARACTER SET utf8  DEFAULT COLLATE utf8_general_ci ;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_registration` (
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
  `ots_users_mapped_to` INT(11) NULL DEFAULT NULL,
  `ots_user_role_id` INT(11) NOT NULL,
  `ots_registration_password` VARCHAR(45) NULL DEFAULT NULL,
  `ots_registration_contact_no` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ots_registration_id`),
  INDEX `fk_ots_registration_ots_users1_idx` (`ots_users_mapped_to` ASC) ,
  INDEX `fk_ots_registration_ots_user_role1_idx` (`ots_user_role_id` ASC) ,
  INDEX `fk_ots_registration_ots_product1_idx` (`ots_product_id` ASC) ,
  UNIQUE INDEX `ots_registration_contact_no_UNIQUE` (`ots_registration_contact_no` ASC) ,
  UNIQUE INDEX `ots_registration_emailid_UNIQUE` (`ots_registration_emailid` ASC) ,
  CONSTRAINT `fk_ots_registration_ots_users1`
    FOREIGN KEY (`ots_users_mapped_to`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_registration_ots_user_role1`
    FOREIGN KEY (`ots_user_role_id`)
    REFERENCES `otsvms_qa`.`ots_user_role` (`ots_user_role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_registration_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_users` (
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
  `ots_registration_id` INT(11) NULL DEFAULT NULL,
  `ots_user_role_id` INT(11) NOT NULL,
  `ots_users_password` VARCHAR(45) NULL DEFAULT NULL,
  `ots_users_contact_no` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`ots_users_id`),
  INDEX `fk_ots_users_ots_registration_idx` (`ots_registration_id` ASC) ,
  INDEX `fk_ots_users_ots_user_role1_idx` (`ots_user_role_id` ASC) ,
  UNIQUE INDEX `ots_users_emailid_UNIQUE` (`ots_users_emailid` ASC) ,
  UNIQUE INDEX `ots_users_contact_no_UNIQUE` (`ots_users_contact_no` ASC) ,
  CONSTRAINT `fk_ots_users_ots_registration`
    FOREIGN KEY (`ots_registration_id`)
    REFERENCES `otsvms_qa`.`ots_registration` (`ots_registration_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_users_ots_user_role1`
    FOREIGN KEY (`ots_user_role_id`)
    REFERENCES `otsvms_qa`.`ots_user_role` (`ots_user_role_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_user_role` (
  `ots_user_role_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_user_role_code` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_name` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_user_role_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_user_role_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_user_role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_user_mapping` (
  `ots_user_mapping_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_mapped_to` INT(11) NULL DEFAULT NULL,
  `ots_users_id` INT(11) NOT NULL,
  `ots_user_mapping_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_user_mapping_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_user_mapping_id`),
  INDEX `fk_ots_user_mapping_ots_users1_idx` (`ots_users_id` ASC) ,
  UNIQUE INDEX `ots_users_id_UNIQUE` (`ots_users_id` ASC) ,
  CONSTRAINT `fk_ots_user_mapping_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_product` (
  `ots_product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_product_name` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_description` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_product_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_product_price` DECIMAL NULL DEFAULT NULL,
  PRIMARY KEY (`ots_product_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_customer_product` (
  `ots_customer_product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_product_id` INT(11) NULL DEFAULT NULL,
  `ots_users_id` INT(11) NOT NULL,
  `ots_customer_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_customer_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_customer_product_price` VARCHAR(55) NULL DEFAULT NULL,
  PRIMARY KEY (`ots_customer_product_id`),
  INDEX `fk_ots_customer_product_ots_product1_idx` (`ots_product_id` ASC) ,
  INDEX `fk_ots_customer_product_ots_users1_idx` (`ots_users_id` ASC) ,
  CONSTRAINT `fk_ots_customer_product_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_customer_product_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_product_stock` (
  `ots_prodcut_stock_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_prodcut_stock_act_qty` VARCHAR(45) NULL DEFAULT NULL,
  `ots_prodcut_stock_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_prodcut_stock_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_prodcut_stock_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_users_id` INT(11) NOT NULL,
  `ots_product_id` INT(11) NOT NULL,
  INDEX `fk_ots_product_stock_ots_users1_idx` (`ots_users_id` ASC) ,
  INDEX `fk_ots_product_stock_ots_product1_idx` (`ots_product_id` ASC) ,
  PRIMARY KEY (`ots_prodcut_stock_id`),
  CONSTRAINT `fk_ots_product_stock_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_product_stock_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_product_stock_history` (
  `ots_product_stock_history_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_product_stock_history_qty` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_stock_history_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_product_stock_history_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_users_id` INT(11) NOT NULL,
  `ots_product_id` INT(11) NOT NULL,
  `ots_product_stock_add_date` DATE NULL DEFAULT NULL,
  `ots_product_stock_order_id` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`ots_product_stock_history_id`),
  INDEX `fk_ots_product_stock_history_ots_users1_idx` (`ots_users_id` ASC) ,
  INDEX `fk_ots_product_stock_history_ots_product1_idx` (`ots_product_id` ASC) ,
  CONSTRAINT `fk_ots_product_stock_history_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_product_stock_history_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_stock_dist_ob` (
  `ots_stock_dist_ob_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_stock_dist_ob_stockdt` DATE NULL DEFAULT NULL,
  `ots_stock_dist_opening_balance` VARCHAR(45) NULL DEFAULT NULL,
  `ots_product_id` INT(11) NOT NULL,
  `ots_users_id` INT(11) NOT NULL,
  PRIMARY KEY (`ots_stock_dist_ob_id`),
  INDEX `fk_ots_stock_dist_ob_ots_product1_idx` (`ots_product_id` ASC) ,
  INDEX `fk_ots_stock_dist_ob_ots_users1_idx` (`ots_users_id` ASC) ,
  CONSTRAINT `fk_ots_stock_dist_ob_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_stock_dist_ob_ots_users1`
    FOREIGN KEY (`ots_users_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_order` (
  `ots_order_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_distributor_id` INT(11) NOT NULL,
  `ots_customer_id` INT(11) NOT NULL,
  `ots_order_number` VARCHAR(45) NULL DEFAULT NULL,
  `ots_assigned_id` INT(11) NULL DEFAULT NULL,
  `ots_order_cost` DECIMAL NULL DEFAULT NULL,
  `ots_order_dt` DATETIME NULL DEFAULT NULL,
  `ots_order_delivery_dt` DATETIME NULL DEFAULT NULL,
  `ots_order_delivered_dt` DATETIME NULL DEFAULT NULL,
  `ots_order_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_order_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_order_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_bill_id` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`ots_order_id`),
  INDEX `fk_ots_order_ots_users1_idx` (`ots_distributor_id` ASC) ,
  INDEX `fk_ots_order_ots_users2_idx` (`ots_customer_id` ASC) ,
  INDEX `fk_ots_order_ots_users3_idx` (`ots_assigned_id` ASC) ,
  INDEX `fk_ots_order_ots_bill1_idx` (`ots_bill_id` ASC) ,
  CONSTRAINT `fk_ots_order_ots_users1`
    FOREIGN KEY (`ots_distributor_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_order_ots_users2`
    FOREIGN KEY (`ots_customer_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_order_ots_users3`
    FOREIGN KEY (`ots_assigned_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_order_ots_bill1`
    FOREIGN KEY (`ots_bill_id`)
    REFERENCES `otsvms_qa`.`ots_bill` (`ots_bill_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_order_product` (
  `ots_order_product_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_order_id` INT(11) NOT NULL,
  `ots_product_id` INT(11) NOT NULL,
  `ots_ordered_qty` INT(11) NULL DEFAULT NULL,
  `ots_delivered_qty` INT(11) NULL DEFAULT NULL,
  `ots_order_product_cost` DECIMAL NULL DEFAULT NULL,
  `ots_order_product_status` VARCHAR(55) NULL DEFAULT NULL,
  `ots_order_product_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_order_product_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_order_product_id`),
  INDEX `fk_ots_order_product_ots_order1_idx` (`ots_order_id` ASC) ,
  INDEX `fk_ots_order_product_ots_product1_idx` (`ots_product_id` ASC) ,
  CONSTRAINT `fk_ots_order_product_ots_order1`
    FOREIGN KEY (`ots_order_id`)
    REFERENCES `otsvms_qa`.`ots_order` (`ots_order_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_ots_order_product_ots_product1`
    FOREIGN KEY (`ots_product_id`)
    REFERENCES `otsvms_qa`.`ots_product` (`ots_product_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_bill` (
  `ots_bill_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_bill_number` VARCHAR(45) NULL DEFAULT NULL,
  `ots_bill_amount` DECIMAL NULL DEFAULT NULL,
  `ots_bill_amount_received` DECIMAL NULL DEFAULT NULL,
  `ots_bill_generated` VARCHAR(45) NULL DEFAULT NULL,
  `ots_bill_status` VARCHAR(45) NULL DEFAULT NULL,
  `ots_bill_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_bill_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_bill_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_lat_lon` (
  `ots_lat_lon_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_latitude` VARCHAR(45) NULL DEFAULT NULL,
  `ots_longitude` VARCHAR(45) NULL DEFAULT NULL,
  `ots_usersusers_id` INT(11) NOT NULL,
  `ots_lat_lon_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  `ots_lat_lon_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_lat_lon_id`),
  INDEX `fk_ots_lat_lon_ots_users1_idx` (`ots_usersusers_id` ASC) ,
  CONSTRAINT `fk_ots_lat_lon_ots_users1`
    FOREIGN KEY (`ots_usersusers_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `otsvms_qa`.`ots_customer_outstanding` (
  `ots_customer_outstanding_id` INT(11) NOT NULL AUTO_INCREMENT,
  `ots_customer_id` INT(11) NOT NULL,
  `ots_customer_outstanding_amt` DECIMAL NULL DEFAULT NULL,
  `ots_customer_outstanding_timestamp` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ots_customer_outstanding_created` DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ots_customer_outstanding_id`),
  INDEX `fk_ots_customer_outstanding_ots_users1_idx` (`ots_customer_id` ASC) ,
  UNIQUE INDEX `ots_customer_id_UNIQUE` (`ots_customer_id` ASC) ,
  CONSTRAINT `fk_ots_customer_outstanding_ots_users1`
    FOREIGN KEY (`ots_customer_id`)
    REFERENCES `otsvms_qa`.`ots_users` (`ots_users_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;



INSERT INTO `ots_user_role` (`ots_user_role_code`, `ots_user_role_name`, `ots_user_role_status`) VALUES ('R001', 'Admin', 'Active');

INSERT INTO `ots_user_role` (`ots_user_role_code`, `ots_user_role_name`, `ots_user_role_status`) VALUES ('R002', 'Distributor', 'Active');

INSERT INTO `ots_user_role` (`ots_user_role_code`, `ots_user_role_name`, `ots_user_role_status`) VALUES ('R003', 'Employee', 'Active');

INSERT INTO `ots_user_role` (`ots_user_role_code`, `ots_user_role_name`, `ots_user_role_status`) VALUES ('R004', 'Customer', 'Active');


INSERT INTO `ots_users` (`ots_users_firstname`, `ots_users_lastname`, `ots_users_addr1`, `ots_users_addr2`, `ots_users_pincode`, `ots_users_emailid`, `ots_users_status`, `ots_user_role_id`, `ots_users_password`, `ots_users_contact_no`) VALUES ('admin', 'ortusolis', 'jayanagar', '4th block', '560076', 'admin@ortusolis.com', 'Active', '1', '123', '7259500665');


CREATE DEFINER=`root`@`localhost` EVENT `DT_product_stock_opening_balance` ON SCHEDULE EVERY 1 DAY STARTS '2019-02-19 01:00:00' ON COMPLETION NOT PRESERVE ENABLE DO INSERT INTO ots_stock_dist_ob (ots_users_id, ots_product_id, ots_stock_dist_opening_balance,ots_stock_dist_ob_stockdt)
	SELECT ots_users_id, ots_product_id, ots_prodcut_stock_act_qty,CURDATE()
	FROM   ots_product_stock;

