CREATE TABLE account_manager (
  id        BIGINT IDENTITY NOT NULL PRIMARY KEY,
  name       VARCHAR(1000)              NOT NULL,
  json_partners       VARCHAR(8000),
  json_end_users    VARCHAR(8000)
);