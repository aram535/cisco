CREATE TABLE promo (
  id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  part_number      VARCHAR(1000),
  description       VARCHAR(1000),
  discount       DOUBLE,
  name    VARCHAR(1000),
  gpl  INT,
  code     VARCHAR(1000),
  claim_per_unit     DOUBLE,
  version        INT
);
