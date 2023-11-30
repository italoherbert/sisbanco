create table conta (
    id int auto_increment,
    titular varchar( 256 ),
    username varchar( 256 ),
    saldo numeric( 10, 2 ),
    credito numeric( 10, 2 ),
    sem_autorizacao_debito_limite numeric( 10, 2 ),
    primary key( id )
);

insert into conta ( id, titular, username, saldo, credito, sem_autorizacao_debito_limite ) values ( 1, 'joao', 'joao', 0, 0, 0 );
insert into conta ( id, titular, username, saldo, credito, sem_autorizacao_debito_limite ) values ( 2, 'jose', 'jose', 0, 0, 0 );
insert into conta ( id, titular, username, saldo, credito, sem_autorizacao_debito_limite ) values ( 3, 'maria', 'maria', 0, 0, 0 );
insert into conta ( id, titular, username, saldo, credito, sem_autorizacao_debito_limite ) values ( 4, 'carlos', 'carlos', 0, 0, 0 );