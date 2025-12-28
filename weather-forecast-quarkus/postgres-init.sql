CREATE TABLE settings
(
    id bigint NOT NULL CONSTRAINT settings_pk PRIMARY KEY,
    url varchar(255) NOT NULL,
    authkey varchar(32) NOT NULL
);

CREATE TABLE geo_location
(
    id bigint NOT NULL CONSTRAINT geo_location_pk PRIMARY KEY,
    latitude numeric(7, 5) NOT NULL,
    longitude numeric(8, 5) NOT NULL,
    country varchar(255) NOT NULL,
    city varchar(255) NOT NULL
);

CREATE SEQUENCE id_seq START 101 CACHE 10;

INSERT INTO settings (id, url, authkey) VALUES (nextval('id_seq'), 'http://api.weatherbit.io', '<32-bit ApiKey>');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 54.69606, 18.67873, 'PL', 'Jastarnia');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 13.10732, -59.62021, 'Barbados', 'Bridgetown');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), -3.71722, -38.54306, 'Brazil', 'Fortaleza');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 34.66942, 32.70132, 'Cyprus', 'Pissouri');
INSERT INTO geo_location (id, latitude, longitude, country, city) VALUES (nextval('id_seq'), 44.92736, 7.71703, 'Mauritius', 'La Morne');
COMMIT;