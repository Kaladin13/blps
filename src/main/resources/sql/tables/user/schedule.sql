--liquibase formatted sql

--changeset rusilee:schedule runInTransaction:false runOnChange:true
create table if not exists schedule
(
    id bigserial primary key,
    date timestamptz not null
);

CREATE OR REPLACE FUNCTION timestamptz_to_date(date TIMESTAMPTZ) RETURNS DATE AS $$
BEGIN
    RETURN CAST(date AS DATE);
END;
$$ LANGUAGE plpgsql IMMUTABLE;

CREATE UNIQUE INDEX IF NOT EXISTS unique_date ON schedule (timestamptz_to_date(date));
