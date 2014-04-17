CREATE TABLE client (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  client_number      VARCHAR(1000)      NOT NULL,
  name       VARCHAR(1000)              NOT NULL,
  city       VARCHAR(1000),
  address    VARCHAR(1000)
);