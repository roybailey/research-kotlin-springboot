create table temp_books
(
    id              serial not null
        constraint temp_books_pkey
            primary key,
    title           varchar(255),
    description     text,
    publicationdate integer,
    price     double precision
);
