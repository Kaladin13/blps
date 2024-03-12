--liquibase formatted sql

--changeset rusilee:schedule runInTransaction:false runOnChange:true
create table if not exists schedule
(
    id bigserial primary key,
    status varchar(50) not null,
    date timestamptz not null
);