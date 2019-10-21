DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id) VALUES
('2019-10-17 09:00:00', 'breakfast', 500, 100000),
('2019-10-17 13:00:00', 'lunch', 1000, 100000),
('2019-10-17 18:00:00', 'dinner', 1000, 100000),
('2019-10-18 10:00:00', 'first', 700, 100000),
('2019-10-18 14:00:00', 'second', 600, 100000),
('2019-10-18 22:00:00', 'third', 500, 100000)
