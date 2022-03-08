package com.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private UserDetailsService usuarioService;
	//Usando la interfaz generica UserDetailsService, 
	//se implementaron sus metodos en el service(componente) anterior llamado UsuarioService  
	
	
	@Autowired
	private AuthenticationEventPublisher eventPublisher;//para el manejo del success and error en el logueo
	
	@Bean
	public static BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); 
	} 
	
	

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService)
		.passwordEncoder(passwordEncoder())
		.and().authenticationEventPublisher(eventPublisher);//este ultimo se agrega para el manejo de success and error
		
	}

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		//necesario para el servidor de autorizacion
		return super.authenticationManager();
	}
	
	
	
	
	

}
