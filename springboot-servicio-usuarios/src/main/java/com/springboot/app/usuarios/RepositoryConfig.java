package com.springboot.app.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.springboot.app.commons.usuarios.models.entity.Usuario; //usando la otra libreria del commons.usuarios
import com.springboot.app.commons.usuarios.models.entity.Rol; //usando la otra libreria del commons.usuarios
//import com.springboot.app.usuarios.models.entity.Rol;
//import com.springboot.app.usuarios.models.entity.Usuario;

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer{

	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(Usuario.class,Rol.class); // si necesitamos que los ids se muestren el en Json
	}
	
	

}
