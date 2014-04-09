CREATE TABLE client (
  id         BIGINT            IDENTITY NOT NULL PRIMARY KEY,
  client_number      VARCHAR(1000)      NOT NULL,
  name       VARCHAR(1000)              NOT NULL,
  city       VARCHAR(1000),
  address    VARCHAR(1000)
);