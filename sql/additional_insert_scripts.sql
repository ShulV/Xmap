------------------------------------------------------------------------------------------------------------------------
-- �������� ������������ (����: �����, �����: a@a.ru, ������: Qwerty)
------------------------------------------------------------------------------------------------------------------------
INSERT INTO public.users(
	id, name, email, pass_hash, phone_number, birthday, reg_date, role, city_id)
	VALUES (1, 'admin_name', 'a@a.ru', '$2a$10$Ref7BqRIHeFPMXdgd06Lb.6bzxY0E07DIu7IDzqsKe3Bo9nuay3U2', '89999999999', '2000.01.01', '2020.01.01', 'ADMIN', 1);
------------------------------------------------------------------------------------------------------------------------
-- �������� ����� �� ����� �����������
------------------------------------------------------------------------------------------------------------------------
-- �������� ���� 1
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('���������� ����', 55.75, 37.62, false, '2001-07-07', null, '�������� �����1', 1, 1, null);
-- �������� ���� 2
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('��������� ����', 59.94, 30.31, false, '2022-07-07', null, '�������� �����2', 2, 1, null);
-- �������� ���� 3
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('������������ ����', 53.36, 83.76, false, '2009-07-07', null, '�������� �����3', 3, 1, null);
-- �������� ���� 4
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('������� ����', 52.54, 85.2, false, '2009-07-07', null, '�������� �����4', 3, 1, null);
-- �������� ���� 5
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('������������� ����', 55.04, 82.93, false, '2009-07-07', null, '�������� �����5', 3, 1, null);
-- �������� ���� 6
insert into spots (name, lat, lon, accepted, adding_date, updating_date, description, space_type_id, user_id, moder_id)
values ('�������� ����', 54.76, 83.1, false, '2009-07-07', null, '�������� �����6', 3, 1, null);


-- �������� ���� ������ 1 2 3 5 ��� ����� 1
insert into spots_sport_types (spot_id, sport_type_id)
values (1, 1), (1, 2), (1, 3), (1, 5);
-- �������� ���� ������ 6 7 ��� ����� 2
insert into spots_sport_types (spot_id, sport_type_id)
values (2, 6), (2, 7);
-- �������� ���� ������ 5 ��� ����� 3
insert into spots_sport_types (spot_id, sport_type_id)
values (3, 5);

-- �������� ���� ������ 1 2 ��� ����� 1
insert into spots_spot_types (spot_id, spot_type_id)
values (1, 1), (1, 2);
-- �������� ���� ������ 1 ��� ����� 2
insert into spots_spot_types (spot_id, spot_type_id)
values (2, 1);
-- �������� ���� ������ 2 ��� ����� 3
insert into spots_spot_types (spot_id, spot_type_id)
values (3, 2);


------------------------------------------------------------------------------------------------------------------------
--
------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------


