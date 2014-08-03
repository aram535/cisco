CREATE TABLE `serial` (
  `serial_number` varchar(100) NOT NULL,
  PRIMARY KEY (`serial_number`),
  UNIQUE KEY `serial_number_UNIQUE` (`serial_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
