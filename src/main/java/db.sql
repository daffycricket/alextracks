use alextracks;
create table incidents(
	incident_id int not null auto_increment, 
	customer_id varchar(100) not null,
	customer_address varchar(1024) not null,
	latitude double,
	longitude double,
	creation_ts timestamp not null,
	primary key(incident_id)
);

insert into incidents values(1,'customer001','79, rue Marcel Dassault 92100 Boulogne Billancourt',null,null,now());
insert into incidents values(2,'customer002','12, rue Troyon 92310 Sèvres',null,null,now());
insert into incidents values(3,'customer003','117 rue du Cherche Midi 75014 Paris',null,null,now());

select * from incidents;
