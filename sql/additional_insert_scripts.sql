------------------------------------------------------------------------------------------------------------------------
-- ВСТАВИТЬ ПОЛЬЗОВАТЕЛЯ (РОЛЬ: АДМИН, ЛОГИН: a@a.ru, ПАРОЛЬ: Qwerty)
------------------------------------------------------------------------------------------------------------------------
INSERT INTO public.users(
	id, name, email, pass_hash, phone_number, birthday, reg_date, role, city_id)
	VALUES (1, 'admin_name', 'a@a.ru', '$2a$10$Ref7BqRIHeFPMXdgd06Lb.6bzxY0E07DIu7IDzqsKe3Bo9nuay3U2', '89999999999', '2000.01.01', '2020.01.01', 'ADMIN', 1);
------------------------------------------------------------------------------------------------------------------------
-- ВСТАВИТЬ СПОТЫ СО ВСЕМИ ПАРАМЕТРАМИ
------------------------------------------------------------------------------------------------------------------------
-- Вставить спот 1
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Московский спот', 55.75, 37.62, false, '2001-07-07', null, 'описание спота1', 1, 1, null);
-- Вставить спот 2
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Питерский спот', 59.94, 30.31, false, '2022-07-07', null, 'описание спота2', 2, 1, null);
-- Вставить спот 3
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Барнаульский спот', 53.36, 83.76, false, '2009-07-07', null, 'описание спота3', 3, 1, null);
-- Вставить спот 4
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Бийский спот', 52.54, 85.2, false, '2009-07-07', null, 'описание спота4', 3, 1, null);
-- Вставить спот 5
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Новосибирский спот', 55.04, 82.93, false, '2009-07-07', null, 'описание спота5', 3, 1, null);
-- Вставить спот 6
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('Бердский спот', 54.76, 83.1, false, '2009-07-07', null, 'описание спота6', 3, 1, null);


-- вставить виды спорта 1 2 3 5 для спота 1
insert into spots_sport_types (spot_id, sport_type_id)
values (1, 1), (1, 2), (1, 3), (1, 5);
-- вставить виды спорта 6 7 для спота 2
insert into spots_sport_types (spot_id, sport_type_id)
values (2, 6), (2, 7);
-- вставить виды спорта 5 для спота 3
insert into spots_sport_types (spot_id, sport_type_id)
values (3, 5);

-- вставить виды спотов 1 2 для спота 1
insert into spots_spot_types (spot_id, spot_type_id)
values (1, 1), (1, 2);
-- вставить виды спотов 1 для спота 2
insert into spots_spot_types (spot_id, spot_type_id)
values (2, 1);
-- вставить виды спотов 2 для спота 3
insert into spots_spot_types (spot_id, spot_type_id)
values (3, 2);


------------------------------------------------------------------------------------------------------------------------
--
------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------


