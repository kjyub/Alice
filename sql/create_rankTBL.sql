use aliceDB;

drop table if exists rankTBL;

create table rankTBL (
	pk int auto_increment,
    id varchar(30) not null unique,
    avr float not null,
    time int not null,
    user_pk int unique,
	foreign key (user_pk) references userTBL(pk),
    primary key (pk)
);

insert into heightTBL(length,leaf0,leaf1,leaf2,x,y,user_pk) values (10,1,2,1,300,300,1);