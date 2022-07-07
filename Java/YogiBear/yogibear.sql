CREATE DATABASE IF NOT EXISTS yogibear;
USE yogibear;
CREATE TABLE IF NOT EXISTS HighScore (
  NameOfPlayer VARCHAR(50) NOT NULL,
  Baskets    INT,
  PRIMARY KEY(NameOfPlayer)
);