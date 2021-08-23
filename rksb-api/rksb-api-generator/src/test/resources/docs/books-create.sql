drop table if exists temp_books;

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

drop view if exists v_temp_books;
create view v_temp_books as select * from temp_books;
