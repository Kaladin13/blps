--liquibase formatted sql

--changeset rusilee:programs runInTransaction:false runOnChange:true
create table if not exists programs
(
    id bigserial primary key,
    schedule_draft_id bigint references schedule (id),
    start_time time not null,
    end_time time not null,
    name varchar(512) not null
)