--liquibase formatted sql

--changeset rusilee:report_queue runInTransaction:false runOnChange:true
create table if not exists report_queue
(
    id           bigserial primary key,
    event        jsonb not null,
    processed_at timestamptz
);