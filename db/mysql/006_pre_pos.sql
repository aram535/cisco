CREATE TABLE PRE_POS (
  id         BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  type      VARCHAR(1000),
  partner_name       VARCHAR(1000),
  part_number       VARCHAR(1000),
  pos_sum  DOUBLE,
  quantity     INT,
  ok     BOOLEAN,
  delta        INT,
  sale_discount INT,
  buy_discount INT,
  sale_price DOUBLE,
  buy_price DOUBLE,
  promo1 VARCHAR(1000),
  promo2 VARCHAR(1000),
  end_user VARCHAR(1000),
  client_number VARCHAR(1000),
  shipped_date DATE,
  shipped_bill_number  VARCHAR(1000),
  comment VARCHAR(1000),
  serials VARCHAR(100000),
  zip INT,
  account_manager_name VARCHAR(1000),
  status VARCHAR(45),
  posready_id varchar(45) DEFAULT NULL,
  claim_id BIGINT DEFAULT 0,
  batch_id BIGINT DEFAULT 0
);
