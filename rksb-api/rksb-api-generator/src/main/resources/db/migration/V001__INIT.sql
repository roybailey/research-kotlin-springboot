DROP TABLE IF EXISTS cities;
CREATE TABLE cities(id serial PRIMARY KEY, name VARCHAR(255), population integer);

DROP TABLE IF EXISTS books;
CREATE TABLE books(id serial PRIMARY KEY, title VARCHAR(255), publicationDate integer);
