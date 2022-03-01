package com.springboot.app.usuarios.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import com.springboot.app.commons.usuarios.models.entity.Usuario; //usando la otra libreria del commons.usuarios


@RepositoryRestResource(path="usuarios")
public interface UsuarioDao extends PagingAndSortingRepository<Usuario, Long>{
	
	@RestResource(path="buscar-username")
	public Usuario findByUsername(String username);
	
	public Usuario findByUsernameAndEmail(String username,String email);
	
	@Query("select u from Usuario u where u.username=?1")
	public Usuario obtenerPorUsername(String username);
	
	@Query("select u from Usuario u where u.username=?1 and u.email=?2")
	public Usuario obtenerPorUsernameEmail(String username,String email);
	

}
