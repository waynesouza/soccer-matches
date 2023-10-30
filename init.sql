create extension if not exists "uuid-ossp";

create table partida (
    id uuid primary key default uuid_generate_v4(),
    time_mandante varchar(100) not null,
    quantidade_gol_mandante integer not null,
    time_visitante varchar(100) not null,
    quantidade_gol_visitante integer not null,
    data date not null,
    horario time not null,
    estadio varchar(100) not null
);