CREATE DATABASE IF NOT EXISTS bookcentre;
USE bookcentre;
CREATE TABLE `client`(
	`client_id` INT PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(100) UNIQUE,
    `password` VARCHAR(100),
    `forename` VARCHAR(30),
    `surname` VARCHAR(30),
    `phone` VARCHAR(9),
    `address` VARCHAR(150)
);

CREATE TABLE `purchase`(
	`purchase_id` INT PRIMARY KEY AUTO_INCREMENT,
    `client_id` INT,
    `is_paid` INT DEFAULT 0,
    `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`client_id`) REFERENCES `client`(`client_id`)
);

CREATE TABLE `book`(
	`book_id` INT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR (100),
    `amount` INT,
    `author` VARCHAR (100),
    `description` VARCHAR (100),
    `price` DECIMAL (5,2)
);

CREATE TABLE `purchase_book`(
	`purchase_id` INT,
    `book_id` INT,
    `amount` INT,
    `version` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`purchase_id`) REFERENCES `purchase`(`purchase_id`),
    FOREIGN KEY (`book_id`) REFERENCES `book`(`book_id`),
    PRIMARY KEY (`purchase_id`,`book_id`)
);

DELIMITER //
CREATE TRIGGER `client_BEFORE_DELETE` BEFORE DELETE 
ON `client`
FOR EACH ROW BEGIN
DELETE FROM `purchase` WHERE `client_id` = OLD.client_id;
END//


CREATE TRIGGER `purchase_BEFORE_DELETE` BEFORE DELETE 
ON `purchase`
FOR EACH ROW BEGIN
DELETE FROM `purchase_book` WHERE `purchase_id` = OLD.purchase_id;
END//
DELIMITER ;

INSERT INTO `bookcentre`.book (`title`,`author`,`description`, `amount`,`price`) VALUES
("A", "Kon", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse ex.",15, 41),
("B", "Bon", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In in pharetra elit. Suspendisse et.",20, 14),
("C", "Ron", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus sodales nulla vel leo aliquam.",20, 14),
("D", "Ton", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ornare orci at nisl pharetra.",20, 14),
("E", "Son", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent dignissim aenean hendrerit.",20, 14);