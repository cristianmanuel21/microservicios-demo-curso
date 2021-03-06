package com.springboot.app.oauth.security;

import java.util.Arrays;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@RefreshScope
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter{
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;//para increptar el secret
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;

	@Override //los permisos de nuestros endpoint del servidor de autorizacion para generar el token y validar el token 
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}
	
	

	
	@Override //configurando los clientes (cualquier usuario de backed, ya sea movil, browser,etc  )
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//clients.inMemory().withClient("frontendapp")
		clients.inMemory().withClient(env.getProperty("config.security.oauth.client.id"))
			//.secret(passwordEncoder.encode("12345"))
		.secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret")))
		    .scopes("read", "write")
            .authorizedGrantTypes("password", "refresh_token")//usando las credenciales del usuario y para obtener un nuevo toker antes que se venza
			.accessTokenValiditySeconds(3600)
			.refreshTokenValiditySeconds(3600);
		//se puede tener mas clientes con angular, react, movil etc
		/*
		.and().withClient("androidapp")
		.secret(passwordEncoder.encode("12345"))
		.scopes("read","wrtite")
		.authorizedGrantTypes("password","refresh_token")//usando las credenciales del usuario y para obtener un nuevo toker antes que se venza
		.accessTokenValiditySeconds(3600)
		.refreshTokenValiditySeconds(3600);
		*/
	}

	
	
	@Override //configurando los tokens
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//agregar informacion extra de los tokens
		
		TokenEnhancerChain tokenEnhancerChain=new  TokenEnhancerChain(); //para agregar la informacion adicional de InfoAdicionalToken
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(JwtAccessTokenConverter(),infoAdicionalToken));
		
		
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(JwtAccessTokenConverter())
		.tokenEnhancer(tokenEnhancerChain);
		
		
		//super.configure(endpoints);
	}
	
	@Bean
	public  JwtTokenStore tokenStore() {
		return new JwtTokenStore(JwtAccessTokenConverter() );
	}

	@Bean
	public JwtAccessTokenConverter JwtAccessTokenConverter() {
		JwtAccessTokenConverter tokenConverter=new JwtAccessTokenConverter();
		//tokenConverter.setSigningKey("poner_codigo_secreto");
		tokenConverter.setSigningKey(Base64.getEncoder()
				.encodeToString(env.getProperty("config.security.oauth.jwt.key").getBytes()) );
		return  tokenConverter;
	}
	
	
	

}
