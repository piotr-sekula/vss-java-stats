create table hashed_passwords(
  uuid UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  password VARCHAR NOT NULL,
  hash_type VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  UNIQUE (uuid)
);

-- Populate some data to hashed_passwords

INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab00', 'MD5', '3288dea5feb7b107b08392eaf1e7aeba');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab11', 'MD5', '2224e3be2d7aef56015b36648143a131');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab22', 'MD5', 'fa9fa397804c34a2935c3ec59b5fe896');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab33', 'MD5', 'a0f39d558a4918ba39eb5d9517ad1373');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab44', 'MD5', '2c3c0cd275815303d130ed7f154efa4c');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab55', 'MD5', '5e16ffadc51ed697af658f0d8832a0fb');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab66', 'MD5', '7f96285cec415f09b6b9b035ae009875');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab77', 'SHA-1', '2781f15ad32a2854c53e29140904450e66da9ef0');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab88', 'SHA-1', 'ba63a7aebd503ae4d77efe8513b6c4cc87fd334e');
INSERT INTO hashed_passwords(uuid, password, hash_type, password_hash) VALUES (gen_random_uuid(), '!ab99', 'SHA-1', '0d3180de9252378056eec608cc0e588e4b1463f2');
