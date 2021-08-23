create table temp_cities
(
    id         serial not null
        constraint temp_cities_pkey
            primary key,
    name       varchar(255),
    population integer
);
