CREATE TABLE SERIAL (
  id        BIGINT IDENTITY NOT NULL PRIMARY KEY,
  serial_number      VARCHAR(1000),
  UNIQUE KEY `serial_number_UNIQUE` (`serial_number`),
);