CREATE TABLE sale (
  id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  shipped_date      DATE,
  shipped_bill_number       VARCHAR(1000),
  client_name       VARCHAR(1000),
  client_number    VARCHAR(1000),
  client_zip   VARCHAR(1000),
  part_number  VARCHAR(1000),
  quantity     INT,
  serials      VARCHAR(100000),
  price        DOUBLE,
  cisco_type   VARCHAR(1000),
  comment      VARCHAR(1000),
  status       VARCHAR(1000)
);