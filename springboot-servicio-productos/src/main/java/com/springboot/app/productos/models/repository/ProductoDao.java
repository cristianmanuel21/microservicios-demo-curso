package com.springboot.app.productos.models.repository;

import org.springframework.data.repository.CrudRepository;

//import com.springboot.app.productos.models.entity.Producto;
import com.springboot.app.commons.models.entity.Producto;

public interface ProductoDao extends CrudRepository<Producto, Long>{

}
