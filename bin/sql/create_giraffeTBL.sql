use aliceDB;

drop table if exists giraffeTBL;

create table giraffeTBL (
	pk int auto_increment,
    id int not null,
    neck int not null,
    birthDate char(12) not null,
    lastDirection int not null,
    lastHeadDirection int not null,
    hungry int not null,
    breed int not null,
    independence int not null,
    x int not null,
    y int not null,
    isMove bool not null,
    isEating bool not null,
    isBreeded bool not null,
    isReflected bool not null,
    isDetected bool not null,
    died bool not null,
    user_pk int,
	foreign key (user_pk) references userTBL(pk),
    primary key (pk)
);