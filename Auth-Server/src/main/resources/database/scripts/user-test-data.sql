INSERT INTO users (id, email, username, password)
VALUES ( -- password
           1, 'user1@gmail.com', 'user1', '{bcrypt}$2a$12$JG6r8yi2yHSYHNgoaQHJOeEhTS9uKavwaNWiNaEFXGVfpJU4l4MIe');

INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1), -- user1 is ADMIN
       (1, 2); -- user1 is USER