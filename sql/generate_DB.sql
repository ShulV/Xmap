------------------------------------------------------------------------------------------------------------------------
-- ���� ������ (����������)
drop table if exists public.spot_types cascade;
CREATE TABLE public.spot_types(
                             id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                             "name" varchar(20) NOT NULL
);
-- ���������� �����������
INSERT INTO public.spot_types(name)
VALUES ('���������'),
       ('�����'),
       ('����'),
       ('���'),
       ('bmx-�������'),
       ('��������');
------------------------------------------------------------------------------------------------------------------------
-- ���� ������ (����������)
drop table if exists public.sport_types cascade;
CREATE TABLE public.sport_types(
                                  id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                  "name" varchar(20) NOT NULL,
                                  transport_name varchar(20) NOT NULL
);
-- ���������� �����������
INSERT INTO public.sport_types(name, transport_name)
VALUES ('bmx-��������', 'bmx'),
       ('������������', '���������'),
       ('�������-��������', '�������'),
       ('������ ���������', 'MTB'),
       ('������-�����', '������'),
       ('������ �����', '����'),
       ('�����������', '��������');
------------------------------------------------------------------------------------------------------------------------
-- ��� ��������� ��� ������������ ��� ������� (����������)
drop table if exists public.space_types cascade;
CREATE TABLE public.space_types(
                                   id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                   "name" varchar(20) NOT NULL
);
-- ���������� �����������
INSERT INTO public.space_types(name)
VALUES ('������ ���������'),
       ('��� �������'),
       ('��� �������� �����');


------------------------------------------------------------------------------------------------------------------------
-- ������
drop table if exists public.countries cascade;
CREATE TABLE public.countries(
                               id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                               "name" varchar(128) NOT NULL
);

------------------------------------------------------------------------------------------------------------------------
-- �������
drop table if exists public.regions cascade;
CREATE TABLE public.regions(
                              id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                              country_id int not null REFERENCES public.countries (id) ON UPDATE CASCADE ON DELETE SET NULL,
                              "name" varchar(128) NOT NULL
);

------------------------------------------------------------------------------------------------------------------------
-- ������
drop table if exists public.cities cascade;
CREATE TABLE public.cities(
                            id int NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                            region_id	int not null REFERENCES regions (id) ON UPDATE CASCADE ON DELETE SET NULL,
                            "name" varchar(128) NOT NULL
);


------------------------------------------------------------------------------------------------------------------------
-- ������������
drop table if exists public.users cascade;
CREATE TABLE public.users(
                       id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       "name" varchar(30) NOT NULL UNIQUE,
                       email varchar(50) NOT NULL UNIQUE,
                       pass_hash varchar(256) NOT NULL,
                       phone_number varchar(15) NOT NULL UNIQUE,
                       birthday date NOT NULL,
                       reg_date date NOT NULL,
                       role varchar(10) NOT NULL, -- �������� � enum, ������� �� ����� administrator ������, ���� ��� - ��������
                       city_id int REFERENCES public.cities (id) ON UPDATE CASCADE ON DELETE SET NULL
);
------------------------------------------------------------------------------------------------------------------------
-- ������ (���� ��� ������ BEARER ��� ������������)
drop table if exists public.token cascade;
CREATE TABLE public.token(
                       id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                       expired BOOLEAN NOT NULL,
                       revoked BOOLEAN NOT NULL,
                       token varchar(255),
                       token_type varchar(255),
                       user_id BIGINT NOT NULL REFERENCES public.users (id) ON UPDATE CASCADE ON DELETE SET NULL
                       );
------------------------------------------------------------------------------------------------------------------------
-- �����
drop table if exists public.spots cascade;
CREATE TABLE public.spots(
                             id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                             "name" varchar(50) NOT NULL,
                             -- latitude coordinate (���������� ������)
                             lat FLOAT NOT NULL,
                             -- longitude coordinate (���������� �������)
                             lon FLOAT NOT NULL,
                             accepted BOOLEAN NOT NULL DEFAULT FALSE,
                             adding_date date NOT NULL,
                             updating_date date,
                             description varchar(300) NOT NULL,
                             space_type_id int NOT NULL REFERENCES public.space_types (id)
                                                           ON UPDATE CASCADE ON DELETE NO ACTION,
                             user_id BIGINT DEFAULT NULL REFERENCES public.users (id)
                                 ON UPDATE CASCADE ON DELETE SET NULL,
                             moder_id BIGINT DEFAULT NULL REFERENCES public.users (id)
                                 ON UPDATE CASCADE ON DELETE SET NULL,
                             city_id int REFERENCES public.cities (id)
                                 ON UPDATE CASCADE ON DELETE SET NULL
);
------------------------------------------------------------------------------------------------------------------------
-- ���������� �� ������������ (������, �������������, )
drop table if exists public.image_info cascade;
CREATE TABLE public.image_info(
                             id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                             orig_name varchar(255) NOT NULL,
                             gen_name varchar(100) NOT NULL,
                             size integer NOT NULL,
                             upload_date date NOT NULL,
                             spot_id BIGINT DEFAULT NULL REFERENCES public.spots (id)
                                 ON UPDATE CASCADE ON DELETE CASCADE,
                             user_id BIGINT DEFAULT NULL REFERENCES public.users (id)
                                 ON UPDATE CASCADE ON DELETE CASCADE
);
------------------------------------------------------------------------------------------------------------------------
-- �����������
drop table if exists public.comments cascade;
CREATE TABLE public.comments(
                            id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                            "text" varchar(100) NOT NULL,
                            upload_date date NOT NULL,
                            spot_id BIGINT NOT NULL REFERENCES public.spots (id)
                              ON UPDATE CASCADE ON DELETE CASCADE,
                            user_id BIGINT NOT NULL REFERENCES public.users (id)
                              ON UPDATE CASCADE ON DELETE CASCADE
);
------------------------------------------------------------------------------------------------------------------------
-- ������������� ������� ����-������������
drop table if exists public.spots_users cascade;
CREATE TABLE public.spots_users(
                               favorite BOOLEAN NOT NULL DEFAULT FALSE,
                               liked BOOLEAN NOT NULL DEFAULT FALSE,
                               spot_id BIGINT NOT NULL REFERENCES public.spots (id)
                                   ON UPDATE CASCADE ON DELETE CASCADE,
                               user_id BIGINT REFERENCES public.users (id)
                                   ON UPDATE CASCADE ON DELETE CASCADE,
                               PRIMARY KEY (spot_id, user_id)
);
------------------------------------------------------------------------------------------------------------------------
-- ������������� ������� ����-���_�����
drop table if exists public.spots_spot_types cascade;
CREATE TABLE public.spots_spot_types(
                                   spot_id BIGINT NOT NULL REFERENCES public.spots (id)
                                       ON UPDATE CASCADE ON DELETE CASCADE,
                                   spot_type_id int REFERENCES public.spot_types (id)
                                                        ON UPDATE CASCADE ON DELETE SET NULL,
                                   PRIMARY KEY (spot_id, spot_type_id)
);
------------------------------------------------------------------------------------------------------------------------
-- ������������� ������� ����-���_������
drop table if exists public.spots_sport_types cascade;
CREATE TABLE public.spots_sport_types(
                                       spot_id BIGINT NOT NULL REFERENCES public.spots (id)
                                           ON UPDATE CASCADE ON DELETE CASCADE,
                                       sport_type_id int REFERENCES public.sport_types (id)
                                                       ON UPDATE CASCADE ON DELETE SET NULL,
                                       PRIMARY KEY (spot_id, sport_type_id)
);
------------------------------------------------------------------------------------------------------------------------
-- ������� ������� ���� ������ ����������� � ������������ ������� (������� �����������)
CREATE OR REPLACE FUNCTION get_spots_in_radius(user_lat DOUBLE PRECISION, user_long DOUBLE PRECISION, raduis DOUBLE PRECISION)
    RETURNS SETOF spots AS $$
BEGIN
    return query
        SELECT * FROM spots as s
        WHERE (((acos(sin(($1*pi()/180)) * sin((s.lat*pi()/180)) +
                      cos(($1*pi()/180)) * cos((s.lat*pi()/180)) *
                      cos((($2 - s.lon) * pi()/180)))) * 180/pi()) * 60 * 1.1515 * 1.609344) < $3;
END;
$$ LANGUAGE plpgsql;
-- ������ �� �������������: SELECT * FROM get_spots_in_radius(53.34, 83.69, 4000.0);
------------------------------------------------------------------------------------------------------------------------

------------------------------------------------------------------------------------------------------------------------