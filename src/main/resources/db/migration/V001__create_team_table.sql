create table if not exists TIME (
    ID bigint not null auto_increment,
    NOME varchar(60) not null,
    ESTADO varchar(2) not null,
    SIGLA varchar(4) not null,
    primary key (ID)
);