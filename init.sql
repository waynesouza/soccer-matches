create extension if not exists "uuid-ossp";

create table time (
    id uuid primary key default uuid_generate_v4(),
    nome varchar(50) not null
);

create table estadio (
    id uuid primary key default uuid_generate_v4(),
    nome varchar(50) not null
);

create table partida (
    id uuid primary key default uuid_generate_v4(),
    time_mandante_id uuid not null,
    qtd_gol_mandante integer not null,
    time_visitante_id uuid not null,
    qtd_gol_visitante integer not null,
    data date not null,
    horario time not null,
    estadio_id uuid not null
);

alter table partida add constraint fk_time_mandante
foreign key (time_mandante_id) references time(id);

alter table partida add constraint fk_time_visitante
foreign key (time_visitante_id) references time(id);