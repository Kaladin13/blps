--liquibase formatted sql

--changeset rusilee:schedule_history runInTransaction:false runOnChange:true
create table if not exists schedule_history
(
    id bigserial primary key,
    schedule_draft_id bigint not null,
    previous_status varchar(50),
    current_status varchar(50) not null,
    updated_at timestamptz not null,
    date timestamptz not null
);