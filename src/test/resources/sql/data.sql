INSERT INTO category (id, name)
VALUES (1, 'Fruit'),
       (2, 'Milk'),
       (3, 'Bread');
SELECT SETVAL('category_id_seq', (SELECT MAX(id) FROM category));


INSERT INTO quality (id, name)
VALUES (1, '1 Сорт'),
       (2, '2 Сорт'),
       (3, 'Брак');
SELECT SETVAL('quality_id_seq', (SELECT MAX(id) FROM quality));


insert into users (id, city, house, postal_code, region, street, birth_day, firstname, image, lastname, password, role,
                   username, type)
values (1, 'Moscow', '1', '123456', 'Central', 'Main 1', '2023-01-01', 'Vasia', null, 'Vasiev', null, 'ADMIN',
        'a@mail.ru', 'person'),
       (2, 'Moscow', '2', '123456', 'Central', 'Main 2', '2023-02-02', 'Ivan', null, 'Ivanov', null, 'PROJECT_MANAGER',
        'p@mail.ru', 'pro_manager'),
       (3, 'Moscow', '3', '123456', 'Central', 'Main 3', '2023-03-03', 'Petr', null, 'Petrov', null, 'MANAGER',
        'm@mail.ru', 'manager'),
       (4, 'Moscow', '4', '123456', 'Central', 'Main 4', '2023-04-04', 'Uri', null, 'Uriev', null, 'STOREKEEPER',
        's@mail.ru', 'person'),
       (5, 'Moscow', '5', '123456', 'Central', 'Main 5', '2023-05-05', 'Olga', null, 'Olgina', null, 'USER',
        'u@mail.ru', 'person');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));


INSERT INTO project (id, name, pro_manager_id)
VALUES (1, '1 Project', 2),
       (2, '2 Project', 2),
       (3, '3 Project', 2);
SELECT SETVAL('project_id_seq', (SELECT MAX(id) FROM project));


INSERT INTO unit (id, name, short_name)
VALUES (1, 'Штука', 'шт'),
       (2, 'Коробка', 'кор'),
       (3, 'Паллет', 'пал');
SELECT SETVAL('unit_id_seq', (SELECT MAX(id) FROM unit));