INSERT INTO users (username, password, enabled)
  VALUES ('john', '{bcrypt}$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 1);

INSERT INTO authorities (username, authority)
  VALUES ('john', 'ROLE_USER');
