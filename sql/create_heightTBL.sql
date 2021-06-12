use aliceDB;

drop table if exists heightTBL;

create table heightTBL (
	pk int auto_increment,
    seq int not null,
    avr float not null,
    user_pk int,
	foreign key (user_pk) references userTBL(pk),
    primary key (pk)
);

insert into heightTBL(length,leaf0,leaf1,leaf2,x,y,user_pk) values (10,1,2,1,300,300,1);