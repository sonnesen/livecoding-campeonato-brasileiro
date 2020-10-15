create table if not exists JOGO (
    ID bigint not null auto_increment,
    DATA datetime not null,
    GOLS_TIME_MANDANTE int default 0,
    GOLS_TIME_VISITANTE int default 0,
    PUBLICO_PAGANTE int default 0,
    ENCERRADO boolean default false,
    RODADA int default 0,
    ID_TIME_MANDANTE bigint not null,
    ID_TIME_VISITANTE bigint not null,
    primary key (ID),
    foreign key (ID_TIME_MANDANTE) references TIME (ID),
    foreign key (ID_TIME_VISITANTE) references TIME (ID)
);