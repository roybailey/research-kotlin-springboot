drop table if exists temp_codegen_sample;

create table temp_codegen_sample
(
    id              serial not null
        constraint temp_codegen_sample_pkey
            primary key,
    name            varchar(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    description     text,
    periodFrom      varchar(10),
    periodUpto      varchar(10),
    price           double precision,
    discount        double precision
);

drop view if exists v_temp_codegen_sample;
create view v_temp_codegen_sample as select * from temp_codegen_sample;

insert into temp_codegen_sample (name,description,periodFrom,periodUpto,price,discount,created_at)
values ('aaa','one','2021-01-01','2021-12-31',1.5,0.05,timestamp '2020-12-31 17:00:00');

insert into temp_codegen_sample (name,description,periodFrom,periodUpto,price,discount,created_at)
values ('bbb','two','2020-07-01','2021-06-30',2.5,0.10,timestamp '2020-06-30 17:00:00');

insert into temp_codegen_sample (name,description,periodFrom,periodUpto,price,discount,created_at)
values ('ccc','three','2020-01-01','2020-12-31',3.5,0.15,timestamp '2019-12-31 17:00:00');

insert into temp_codegen_sample (name,description,periodFrom,periodUpto,price,discount,created_at)
values ('ddd','four','2019-07-01','2020-06-30',4.5,0.20,timestamp '2019-06-30 17:00:00');

insert into temp_codegen_sample (name,description,periodFrom,periodUpto,price,discount,created_at)
values ('eee','five','2019-01-01','2019-12-31',5.5,0.25,timestamp '2018-12-31 17:00:00');
