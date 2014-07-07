CREATE TABLE pricelist (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  part_number VARCHAR(1000)      NOT NULL,
  description      VARCHAR(1000)      NOT NULL,
  gpl       DOUBLE              NOT NULL,
  wpl       DOUBLE,
  discount    DOUBLE
);