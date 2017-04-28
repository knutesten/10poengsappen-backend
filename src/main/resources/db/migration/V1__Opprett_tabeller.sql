CREATE TABLE user (
  id    INT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL,
  name  VARCHAR(255) NOT NULL,
);

CREATE TABLE team (
  id   INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
);

CREATE TABLE user_team (
  user_id INT NOT NULL,
  team_id INT NOT NULL,
  PRIMARY KEY (user_id, team_id)
);

CREATE TABLE points (
  id          INT PRIMARY KEY AUTO_INCREMENT,
  created_at  TIMESTAMP NOT NULL,
  receiver_id INT       NOT NULL REFERENCES user (id),
  giver_id    INT       NOT NULL REFERENCES user (id),
  team_id     INT       NOT NULL REFERENCES team (id),
  amount      INT       NOT NULL
);