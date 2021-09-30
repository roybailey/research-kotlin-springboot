drop table if exists temp_codegen_sample;

create table temp_codegen_sample
(
    id              serial not null
        constraint temp_codegen_sample_pkey
            primary key,
    title           varchar(255),
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

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('aaa','one','2021-01-01','2021-12-31',1.5,0.05,timestamp '2020-12-31 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('bbb','two','2020-07-01','2021-06-30',2.5,0.10,timestamp '2020-06-30 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('ccc','three','2020-01-01','2020-12-31',3.5,0.15,timestamp '2019-12-31 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('ddd','four','2019-07-01','2020-06-30',4.5,0.20,timestamp '2019-06-30 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('eee','five','2019-01-01','2019-12-31',5.5,0.25,timestamp '2018-12-31 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('fff','six','2018-07-01','2019-06-30',6.5,0.35,timestamp '2018-06-30 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('ggg','seven','2018-01-01','2018-12-31',7.5,0.45,timestamp '2017-12-31 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('hhh','eight','2017-07-01','2018-06-30',8.5,0.55,timestamp '2017-06-30 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('iii','nine','2017-01-01','2017-12-31',9.5,0.65,timestamp '2016-12-31 17:00:00');

insert into temp_codegen_sample (title,description,periodFrom,periodUpto,price,discount,created_at)
values ('jjj','ten','2016-07-31','2017-06-39',10.5,0.75,timestamp '2016-06-30 17:00:00');
