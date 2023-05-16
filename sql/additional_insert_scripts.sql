------------------------------------------------------------------------------------------------------------------------
-- ВСТАВИТЬ 3 СПОТА СО ВСЕМИ ПАРАМЕТРАМИ
------------------------------------------------------------------------------------------------------------------------
-- Вставить спот 1
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('название спота1', 25.12, 199.3, false, '2001-07-07', null, 'описание спота1', 1, 1, null);
-- Вставить спот 2
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('название спота2', 35.12, 139.3, false, '2022-07-07', null, 'описание спота2', 2, 1, null);
-- Вставить спот 3
insert into spots (name, lat, lon, accepted, adding_date, updating_date, "desc", space_type_id, user_id, moder_id)
values ('название спота3', 5.12, 99.321, false, '2009-07-07', null, 'описание спота3', 3, 1, null);

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


