DROP TABLE IF EXISTS temp_cities;
CREATE TABLE temp_cities(id serial PRIMARY KEY, name VARCHAR(255), population integer);

DROP TABLE IF EXISTS temp_books;
CREATE TABLE temp_books(id serial PRIMARY KEY, title VARCHAR(255), publicationDate integer);
