package com.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher{
	
	private static Logger log=LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	
	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		
		
//		if(authentication.getName().equalsIgnoreCase("frontendapp")) { //ambos filtran el client.id
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
			return;
		}
		
		
		UserDetails user = (UserDetails) authentication.getPrincipal();
		String mensaje="success Login: "+user.getUsername();
		System.out.println(mensaje);
		log.info(mensaje);
		
		Usuario usuario= usuarioService.findByUsername(authentication.getName());
		if(usuario.getGetIntentos()!=null && usuario.getGetIntentos()>0) {
			usuario.setGetIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}

	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		String mensaje="Error en el login: "+exception.getMessage();
		log.error(mensaje);
		System.out.println(mensaje);
		
		try {
		Usuario usuario= usuarioService.findByUsername(authentication.getName());
		if(usuario.getGetIntentos()==null) {
			usuario.setGetIntentos(0);
		}
		
		log.error("Intentos actuales es "+usuario.getGetIntentos());
		
		usuario.setGetIntentos(usuario.getGetIntentos()+1);
		
		log.error("Intentos actuales es "+usuario.getGetIntentos());
		
		log.error(String.format("El usuario %s no existe en el sistema ", authentication.getName()));
		
		if(usuario.getGetIntentos()>=3) {
			usuario.setEnabled(false);
		}
		
		usuarioService.update(usuario, usuario.getId());
		
		}catch(FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema ", authentication.getName()));
		}
		
	}
	
	

}
