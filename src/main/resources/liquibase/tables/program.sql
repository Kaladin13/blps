--liquibase formatted sql

--changeset rusilee:program runInTransaction:false runOnChange:true
create table if not exists program
(
    id bigserial primary key,
    schedule_id bigint references schedule (id) not null,
    start_time timestamptz not null,
    end_time timestamptz not null,
    name varchar(512) not null
)