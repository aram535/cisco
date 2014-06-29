CREATE TABLE account_manager (
  id        BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name       VARCHAR(1000)              NOT NULL,
  json_partners       VARCHAR(8000),
  json_end_users    VARCHAR(8000)
);