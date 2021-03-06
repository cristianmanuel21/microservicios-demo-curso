package com.springboot.app.oauth.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.app.commons.usuarios.models.entity.Usuario;

@FeignClient(name="servicio-usuarios")
public interface UsuarioFeignClient {
	
	
	@GetMapping("/usuarios/search/buscar-username") // 'search' es la forma correcta porque usamos el RepositoryRestResource en el microservicio usuarios 
	public Usuario findByUsername(@RequestParam String username);
	
	
	@PutMapping("/usuarios/{id}")
	public Usuario updateUsuario(@RequestBody Usuario usuario, @PathVariable Long id);

}
