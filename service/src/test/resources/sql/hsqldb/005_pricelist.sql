CREATE TABLE pricelist (
  id        BIGINT IDENTITY NOT NULL PRIMARY KEY,
  part_number VARCHAR(1000)      NOT NULL,
  description      VARCHAR(1000)      NOT NULL,
  gpl       INT              NOT NULL,
  wpl       DOUBLE,
  discount    DOUBLE
);