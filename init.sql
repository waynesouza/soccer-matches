create table equipe (
    id char(36) not null primary key,
    nome varchar(100) not null
);

create table estadio (
    id char(36) not null primary key,
    nome varchar(100) not null
);

create table partida (
    id char(36) not null primary key,
    equipe_mandante_id char(36) not null,
    equipe_visitante_id char(36) not null,
    quantidade_gol_mandante integer not null,
    quantidade_gol_visitante integer not null,
    resultado char(1) not null,
    data_hora datetime not null,
    estadio_id char(36) not null,
    foreign key (equipe_mandante_id) references equipe(id),
    foreign key (equipe_visitante_id) references equipe(id),
    foreign key (estadio_id) references estadio(id),
    check (resultado in ('V', 'E', 'D'))
);

create index idx_equipe_id on equipe (id);
create index idx_estadio_id on estadio (id);
create index idx_partida_id on partida (id);