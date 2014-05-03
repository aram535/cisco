CREATE TABLE promo (
  id         BIGINT IDENTITY NOT NULL PRIMARY KEY,
  part_number      VARCHAR(1000),
  description       VARCHAR(1000),
  discount       DOUBLE,
  name    VARCHAR(1000),
  gpl  INT,
  code     VARCHAR(1000),
  claim_per_unit     DOUBLE,
  version        INT,
  end_date DATE
);
