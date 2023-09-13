CREATE TABLE IF NOT EXISTS `refresh_tokens`
(
    `id` INT PRIMARY KEY AUTO_INCREMENT,
    `token` VARCHAR(160) NOT NULL,
    `username` VARCHAR(90) NOT NULL,
    CONSTRAINT `fk_username` FOREIGN KEY (`username`) REFERENCES `users` (`email`)
);