DROP TABLE people IF EXISTS;

CREATE TABLE people  (
                         person_id BIGINT NOT NULL PRIMARY KEY generated always as identity ,
                         first_name VARCHAR(20),
                         last_name VARCHAR(20)
);

CREATE TABLE source_code (
    code_id  BIGINT NOT NULL PRIMARY KEY generated always as identity ,
    filename varchar(150),
    content TEXT
);
