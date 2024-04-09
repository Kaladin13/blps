--liquibase formatted sql

--changeset rusilee:schedule runInTransaction:false runOnChange:true
create table if not exists schedule
(
    id bigserial primary key,
    date timestamptz not null
);