package com.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.client.UsuarioFeignClient;

import feign.FeignException;

//una clase especial propia de SpringSecurity para autenticacion, donde usamos el Feign escrito anteriormente
@Service
public class UsuarioService implements IUsuarioService, UserDetailsService{
	
	private Logger log=LoggerFactory.getLogger(UsuarioService.class);
	
	
	@Autowired
	private UsuarioFeignClient client;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
	try {
		//este metodo retorna (UserDetails) que es un tipo de usuario de Spring Security autenticado
		Usuario usuario=client.findByUsername(username);
		List<GrantedAuthority> authorities=usuario.getRoles()
				.stream()
				.map(role-> new SimpleGrantedAuthority(role.getNombre()))
				.peek(auth-> log.info("Rol "+auth.getAuthority()))
				.collect(Collectors.toList());
		
		log.info("Usuario Autentificado "+username+" con clave "+usuario.getPassword());
		
		
		return new User(usuario.getUsername(), usuario.getPassword()
				, usuario.getEnabled(), true, true, true, authorities  );
		
	}catch(FeignException e) {
		log.error("Error en el login, el usuario "+username+" no existe");
		throw new UsernameNotFoundException
		("Error en el login, el usuario "+username+" no existe");
	}	
	
	}

	@Override
	public Usuario findByUsername(String username) {
		// TODO Auto-generated method stub
		return client.findByUsername(username);
	}

	@Override 
	public Usuario update(Usuario usuario, Long id) { //para actualizar los intentos 
		return client.updateUsuario(usuario, id);
	}

}
