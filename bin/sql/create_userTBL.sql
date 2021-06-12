use aliceDB;

drop table if exists userTBL;

create table userTBL (
	pk int auto_increment,
    id varchar(30) unique not null,
    pw varchar(30) not null,
    loggedIn bool default false,
    timeStamp long not null,
    timeStopFlag bool not null,
    maxGiraffeID int not null,
    searchScale int not null,
    ageRate int not null,
    breedReadyValue int not null,
    breedValue int not null,
    maxIndependence int not null,
    started bool not null,
    mutantProb int not null,
    sizeScale float not null,
    subjectInfo bool not null,
    primary key(pk)
);

select * from userTBL where id="kjy" and pw="aa";