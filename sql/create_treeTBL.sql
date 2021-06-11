use aliceDB;

drop table if exists treeTBL;

create table treeTBL (
	pk int auto_increment,
    length int not null,
    leaf0 int not null,
    leaf1 int not null,
    leaf2 int not null,
    x int not null,
    y int not null,
    user_pk int,
	foreign key (user_pk) references userTBL(pk),
    primary key (pk)
);

insert into treeTBL(length,leaf0,leaf1,leaf2,x,y,user_pk) values (10,1,2,1,300,300,1);