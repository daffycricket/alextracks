use alextracks;
drop table incidents; 

create table incidents(
	incident_id int not null auto_increment, 
	customer_id varchar(100) not null,
	customer_address text not null,
	creation_ts timestamp not null,
	latitude double,
	longitude double,
	formatted_address text,
	primary key(incident_id)
);

insert into incidents values(1, 'customer001', '79 RUE Marcel Dassault 92100 Boulogne Billancourt', now(), 1.0, 0.0, '79, rue Marcel Dassault 92100, Boulogne-Billancourt');
insert into incidents values(2, 'customer002', '12  rue Troyon 92310 Sèvres', now(), 2.0, 1.0, '12, rue Troyon 92310, Sèvres');
insert into incidents values(3, 'customer003', '117 rue du Cherche Midi 75014 Paris', now(), 5.0, 6.0, '117, rue du Cherche Midi 75014, Paris');

select * from incidents;

