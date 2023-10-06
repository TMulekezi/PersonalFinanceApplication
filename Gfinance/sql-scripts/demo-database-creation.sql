CREATE DATABASE IF NOT EXISTS `gfinance_db`;
USE `gfinance_db`;

-- drop all tables
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `transaction`;
DROP TABLE IF EXISTS `savings`;
DROP TABLE IF EXISTS `checking`;
DROP TABLE IF EXISTS `savings_goal`;
DROP TABLE IF EXISTS `investment`;
DROP TABLE IF EXISTS `budget_streak`;
DROP TABLE IF EXISTS `savings_streak`;
DROP TRIGGER IF exists `setdatebudgetstreak`;
DROP TRIGGER IF exists `setdatesavingsstreak`;
DROP TABLE IF EXISTS `achievement`;
DROP TABLE IF EXISTS `users_achievements`;
DROP TABLE IF EXISTS `reoccurring`;
DROP TRIGGER IF EXISTS `setdatereoccurring`;
SET foreign_key_checks = 1;

-- create tables and insert values
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(68) NOT NULL,
  `enabled` tinyint NOT NULL default 1,
  PRIMARY KEY (`id`),
  
  UNIQUE KEY `usergf_idx_1` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- password is test123
INSERT INTO `user` (`username`, `password`, `enabled`) VALUES
('tom', '$2a$10$27DO34ulV3RMgo4S.G369e0h7OECx7VuriE8UlutnasiCP2RP1phq', 1),
('admin', '$2a$10$27DO34ulV3RMgo4S.G369e0h7OECx7VuriE8UlutnasiCP2RP1phq', 1);

CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `rolename` varchar(50) NOT NULL,
  UNIQUE KEY `rolegf_idx_1` (`id`,`rolename`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=latin1;


INSERT INTO `role` (`rolename`) VALUES 
	('ROLE_USER'), 
    ('ROLE_ADMIN');


CREATE TABLE `users_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_05` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `users_roles` (user_id,role_id)
VALUES 
(1, 1), (2,2);

-- add amount column 
CREATE TABLE `transaction` (
	`id` integer NOT NULL auto_increment,
    `name` varchar(50) NOT NULL,
    `recurring` tinyint NOT NULL default 0,
    `expense` tinyint NOT NULL default 1,
    `essential` tinyint NOT NULL default 1,
    `type` varchar(50) NOT NULL,
    `date` DATETIME DEFAULT current_timestamp NOT NULL,
    `amount` decimal(14, 2),
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_transaction1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
     
 insert into `transaction` (`name`,`recurring`,`essential`,`type`, `amount`, `user_id`) VALUES
	('water bill',0,1,'bills', 10.00, 1),
	('movie',0,1,'leisure', 10.00, 1),
--     ('golf',0,1,'leisure', 5.00, 1),
--     ('uber',0,0,'transport', 7.00,1),
--     ('train',0,1,'transport', 3.50,1),
--     ('electricity',0,0,'bills', 15.00,1),
--     ('pool',0,1,'leisure', 15.00,1),
--     ('groceries',0,1,'food', 11.00,1),
--     ('venmo',0,0,'transfer', 5.00,1),
--     ('uber',0,1,'transport', 12.00,1),
    ('groceries',0,0,'shopping', 15.00,1),
    ('haircut',0,1,'other', 7.00,1),
    ('bus',0,1,'transport', 2.00,1)
    ;
    
     insert into `transaction` (`name`,`recurring`,`essential`,`type`,`date`, `amount`, `user_id`) VALUES
	('water bill',0,1,'bills', '2023-08-28 10:34:09', 10.00, 1),
	('movie',0,1,'leisure', '2023-08-28 10:34:09', 10.00, 1),
    ('golf',0,1,'leisure', '2023-08-28 10:34:09', 5.00, 1),
    ('uber',0,0,'transport', '2023-08-28 10:34:09', 7.00,1),
    ('train',0,1,'transport', '2023-08-28 10:34:09', 3.50,1),
    
    ('gift',0,0,'other', '2023-08-29 10:34:09', 15.00, 1),
	('paypal',0,1,'transfer', '2023-08-29 10:34:09', 10.00, 1),
    ('donation',0,0,'other', '2023-08-29 10:34:09', 5.00, 1),
    ('bus',0,0,'transport', '2023-08-29 10:34:09', 7.00,1),
    ('movie',0,1,'leisure', '2023-08-29 10:34:09', 3.50,1),
    
    ('clothes',0,1,'shopping', '2023-08-30 10:34:09', 10.00, 1),
	('burger',0,0,'food', '2023-08-30 10:34:09', 10.00, 1),
    ('golf',0,1,'leisure', '2023-08-30 10:34:09', 15.00, 1),
    ('gas',0,0,'transport', '2023-08-30 10:34:09', 50,1),
    ('train',0,1,'transport', '2023-08-30 10:34:09', 3.50,1),
    
    ('gas bill',0,1,'bills', '2023-08-31 10:34:09', 20.00, 1),
	('phone',0,1,'shopping', '2023-08-31 10:34:09', 10.00, 1),
    ('golf',0,0,'leisure', '2023-08-31 10:34:09', 5.00, 1),
    ('dog treats',0,1,'other', '2023-08-31 10:34:09', 7.00,1),
    ('venmo',0,1,'transfer', '2023-08-31 10:34:09', 3.50,1),
    
     ('gift',0,0,'other', '2023-09-01 10:34:09', 15.00, 1),
	('paypal',0,1,'transfer', '2023-09-01 10:34:09', 10.00, 1),
    ('donation',0,0,'other', '2023-09-01 10:34:09', 5.00, 1),
    ('bus',0,0,'transport', '2023-09-01 10:34:09', 7.00,1),
    ('movie',0,1,'leisure', '2023-09-01 10:34:09', 3.50,1),
    
    ('clothes',0,1,'shopping', '2023-09-02 10:34:09', 10.00, 1),
	('burger',0,0,'food', '2023-09-02 10:34:09', 10.00, 1),
    ('golf',0,1,'leisure', '2023-09-02 10:34:09', 15.00, 1),
    ('gas',0,0,'transport', '2023-09-02 10:34:09', 50,1),
    ('train',0,1,'transport', '2023-09-02 10:34:09', 3.50,1),
    
    ('gas bill',0,1,'bills', '2023-09-03 10:34:09', 20.00, 1),
	('phone',0,1,'shopping', '2023-09-03 10:34:09', 10.00, 1),
    ('golf',0,0,'leisure', '2023-09-03 10:34:09', 5.00, 1),
    ('dog treats',0,1,'other', '2023-09-03 10:34:09', 7.00,1),
    ('venmo',0,1,'transfer', '2023-09-03 10:34:09', 3.50,1)
    ;

    
     
CREATE TABLE `checking` (
	`id` integer not null AUTO_INCREMENT,
    `amount` double not null default 0,
    `budget` double not null default 100,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_checking1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `checking` (`amount`, `user_id`) VALUES
	(2000.00, 1),
    (0.00, 2)
;
     
 CREATE TABLE `savings` (
	`id` integer not null AUTO_INCREMENT,
    `amount` double not null default 0,
    `savings_target` double not null default 10,
    `savings_contribution` double not null default 0,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_savings1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
     )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
    
INSERT INTO `savings` (`amount`, `user_id`) VALUES 
	(400, 1),
    (0, 2)
;

CREATE TABLE `savings_goal` (
	`id` integer not null AUTO_INCREMENT,
    `amount` double not null default 0,
    `name` varchar(50) NOT NULL,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_savings_goal1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

INSERT INTO `savings_goal`(`amount`,`name`,`user_id`) VALUES
(100, 'car', 1),
(10000, 'house', 1),
(1000, 'xmas', 1);

CREATE TABLE `investment` (
	`id` integer not null AUTO_INCREMENT,
    `symbol` varchar(50) NOT NULL,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_transaction_goal1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;





CREATE TABLE `budget_streak` (
	`id` integer NOT NULL auto_increment,
    `record_streak` integer NOT NULL default 0,
    `current_streak` integer NOT NULL default 0,
    `date_last_confirmed` timestamp not null,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_budget_streak1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DELIMITER $$
CREATE TRIGGER `setdatebudgetstreak`
BEFORE INSERT ON `budget_streak` 
FOR EACH ROW BEGIN
   IF new.`date_last_confirmed` is null THEN
    SET new.`date_last_confirmed` = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
    END IF;
END$$
DELIMITER ;
     
 INSERT INTO `budget_streak`(`record_streak`, `current_streak`, `user_id`) VALUES
	(7, 2, 1),
    (0,0,2)
    ;
     
CREATE TABLE `savings_streak` (
	`id` integer NOT NULL auto_increment,
    `record_streak` integer NOT NULL default 0,
    `current_streak` integer NOT NULL default 0,
    `date_last_confirmed` timestamp not null,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_savings_streak1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
     ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;
     
DELIMITER $$
CREATE TRIGGER `setdatesavingsstreak`
BEFORE INSERT ON `savings_streak` 
FOR EACH ROW BEGIN
   IF new.`date_last_confirmed` is null THEN
    SET new.`date_last_confirmed` = DATE_SUB(CURDATE(), INTERVAL 1 DAY);
    END IF;
END$$
DELIMITER ;
     
 INSERT INTO `savings_streak`(`record_streak`, `current_streak`, `user_id`) VALUES
	(20, 10, 1),
    (0,0,2);
    
    
CREATE TABLE `achievement` (
  `id` int NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `achievement_name` varchar(50) NOT NULL,
  `achievement_icon` varchar(50) not NULL,
  `target` integer not null,  
  -- UNIQUE KEY `achievement_index` (`id`,`rolename`)
  PRIMARY KEY(`id`)
) ENGINE=InnoDB auto_increment=1 DEFAULT CHARSET=latin1;

INSERT INTO `achievement`(`achievement_name`,`achievement_icon`,`type`,`target`) VALUES
('Savings novice', 'directions_walk' ,'savings', 10),
('Savings apprectice', 'hiking','savings', 20),
('Savings master', 'directions_run','savings',30),
('Budget novice', 'flight_takeoff','budget', 10),
('Budget apprectice', 'flight','budget', 20),
('Budget master', 'flight_land','budget', 30);

CREATE TABLE `users_achievements` (
  `user_id` int(11) NOT NULL,
  `achievement_id` int(11) NOT NULL,
  
  PRIMARY KEY (`user_id`,`achievement_id`),
  
  KEY `index_users_achievements_1` (`achievement_id`),
  
  CONSTRAINT `fk_users_achievements_3` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION,
  
  CONSTRAINT `fk_users_achievements_4` FOREIGN KEY (`achievement_id`) 
  REFERENCES `achievement` (`id`) 
  ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `users_achievements` (`user_id`,`achievement_id`)
VALUES 
(1, 1),
(1, 2), 
(1, 3),
(1, 4),
(1, 5), 
(1, 6),
(2, 1),
(2, 2), 
(2, 3),
(2, 4),
(2, 5), 
(2, 6);

CREATE TABLE `reoccurring` (
	`id` integer not null AUTO_INCREMENT,
    `amount` double not null default 0,
    `expense` tinyint not null default 1,
    `essential` tinyint not null default 1,
    `type` varchar(50) NOT NULL default 'other',
    `name` varchar(50) NOT NULL,
    `renew_date` timestamp default current_timestamp NOT NULL,
    `date_last_renewed` timestamp NOT NULL,
    `last_payment_failed` boolean default false not null,
    `user_id` integer NOT NULL,
     PRIMARY KEY (`id`),
     CONSTRAINT `fk_reoccurring1` FOREIGN KEY (`user_id`)
     REFERENCES `user`(`id`)
)ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

DELIMITER $$
CREATE TRIGGER `setdatereoccurring`
BEFORE INSERT ON `reoccurring` 
FOR EACH ROW BEGIN
   IF new.`date_last_renewed` is null THEN
    SET new.`date_last_renewed` = DATE_SUB(CURDATE(), INTERVAL 1 MONTH);
    END IF;
END$$
DELIMITER ;

INSERT INTO `reoccurring`(`amount`, `name`, `user_id`, `expense`, `type`) values
(5.99, 'spotify', 1, 1, 'leisure');


INSERT INTO `reoccurring`(`amount`, `name`, `user_id`, `renew_date`, `date_last_renewed`) values 
(20.00, 'shampoo', 1, '2022-09-11 10:34:09', '2022-08-11 10:34:09'), (9.99, 'netflix', 1, '2023-10-11 10:34:09', '2023-10-11 10:34:09');
