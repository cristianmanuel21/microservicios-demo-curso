insert into usuarios (username,password,enabled,nombre,apellido,email) values ('paul','$2a$10$qZmr76e8W5hBdKr.i9ersOf6uajvoSkvhHhlwCFAQFDQnwX0x/MRK',1,'Paul','Llanos','paul_ll3@gmail.com');
insert into usuarios (username,password,enabled,nombre,apellido,email) values ('admin','$2a$10$znypVHSrbumTHvFrkDpP2eOP96yFaKrwXpX/3p1LBu7P7DqjYoQKe',1,'Cristian','Llanos','cristianmanuel.21@gmail.com');

insert into roles (nombre) values ('ROLE_USER');
insert into roles (nombre) values ('ROLE_ADMIN');

insert into usuarios_roles (usuario_id,rol_id) values (1,1);
insert into usuarios_roles (usuario_id,rol_id) values (2,2);
insert into usuarios_roles (usuario_id,rol_id) values (2,1);