create table conta (
    id int auto_increment,
    titular varchar( 256 ),
    username varchar( 256 ),
    saldo numeric( 10, 2 ),
    credito numeric( 10, 2 ),
    primary key( id )
);