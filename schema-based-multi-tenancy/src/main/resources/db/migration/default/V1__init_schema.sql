CREATE TABLE user
(
    id                 BIGINT AUTO_INCREMENT,
    username           VARCHAR(255) UNIQUE,
    password           VARCHAR(255)
);
