INSERT INTO users (id, username, password)
VALUES ( -- password
           1, 'user1', '{bcrypt}$2a$12$JG6r8yi2yHSYHNgoaQHJOeEhTS9uKavwaNWiNaEFXGVfpJU4l4MIe');

INSERT INTO users_authorities (user_id, authority)
VALUES (1, 'ADMIN'),
       (1, 'USER');
  