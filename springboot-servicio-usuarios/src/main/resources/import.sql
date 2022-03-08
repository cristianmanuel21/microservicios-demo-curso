insert into usuarios (username,password,enabled,nombre,apellido,email) values ('paul','$2a$10$OFpCwK2MF9lu5Gm7NjJn1utyK968lrz6Qxf2h5rhAYxW06yBfMc66',1,'Paul','Llanos','paul_ll3@gmail.com');
insert into usuarios (username,password,enabled,nombre,apellido,email) values ('admin','$2a$10$Snu73xEvuATor7hx8CUmbuyNYL.XVipgGC8FyIoOsVfMpJv.jWZOO',1,'Cristian','Llanos','cristianmanuel.21@gmail.com');
 

insert into roles (nombre) values ('ROLE_USER');
insert into roles (nombre) values ('ROLE_ADMIN');

insert into usuarios_roles (usuario_id,rol_id) values (1,1);
insert into usuarios_roles (usuario_id,rol_id) values (2,2);
insert into usuarios_roles (usuario_id,rol_id) values (2,1);