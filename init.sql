create table partida (
    id char(36) not null primary key,
    time_mandante varchar(100) not null,
    quantidade_gol_mandante integer not null,
    time_visitante varchar(100) not null,
    quantidade_gol_visitante integer not null,
    data_hora datetime not null,
    estadio varchar(100) not null
);

create index idx_partida_id on partida (id);