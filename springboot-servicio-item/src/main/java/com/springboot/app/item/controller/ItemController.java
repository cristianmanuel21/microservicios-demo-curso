package com.springboot.app.item.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.app.item.models.Item;
import com.springboot.app.commons.models.entity.Producto;
import com.springboot.app.item.models.service.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;


@RefreshScope
@RestController
public class ItemController {
	
	private final Logger log=LoggerFactory.getLogger(ItemController.class);
	
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Autowired
	private Environment env;

	/*
	
	@Value("${configuracion.texto}")
	private String texto;
	*/
	
	
	@Autowired
	@Qualifier("serviceFeign")
	//@Qualifier("serviceRestTemplate")
	private ItemService itemService;
	
	@GetMapping("/listar")
	public List<Item> listar(@RequestParam(name="nombre",required=false) String nombre,@RequestHeader(name="token-request",required=false) String token){
		System.out.println(nombre);
		System.out.println(token);
		return itemService.findAll();
	}
	
	//@HystrixCommand(fallbackMethod="metodoAlternativo")
	/*
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id,@PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
	}*/
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable Long id,@PathVariable Integer cantidad) {
		return cbFactory.create("items")
				.run(()-> itemService.findById(id, cantidad), e -> metodoAlternativo(id,cantidad,e) );
				
	}
	
	@CircuitBreaker(name="items",fallbackMethod="metodoAlternativo")//solo funciona usando application.yml
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable Long id,@PathVariable Integer cantidad) {
		return itemService.findById(id, cantidad);
				
	}
	
	public Item metodoAlternativo(Long id,Integer cantidad,Throwable e) {
		log.info(e.getMessage());
		Item item=new Item();
		Producto p=new Producto();
		item.setCantidad(cantidad);
		p.setId(id);
		p.setNombre("Producto de prueba");
		p.setPrecio(1.00);
		item.setProducto(p);
		return item;
	}
	
	
	@CircuitBreaker(name="items",fallbackMethod = "metodoAlternativo2")
	@TimeLimiter(name="items")
	//@TimeLimiter(name="items",fallbackMethod = "metodoAlternativo2")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable Long id,@PathVariable Integer cantidad) {
		return CompletableFuture.supplyAsync(()-> itemService.findById(id, cantidad));
				//supplyAsync( () -> itemService.findById(id, cantidad) );
	}
	
	public CompletableFuture<Item> metodoAlternativo2(Long id,Integer cantidad,Throwable e) {
		log.info(e.getMessage());
		Item item=new Item();
		Producto p=new Producto();
		item.setCantidad(cantidad);
		p.setId(id);
		p.setNombre("Producto de prueba TimeLimiter");
		p.setPrecio(1.00);
		item.setProducto(p);
		return CompletableFuture.supplyAsync(()->item);
	}
	
	
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto,@PathVariable Long id) {
		return itemService.update(producto, id);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		itemService.delete(id);
	}
	
	
	/*
	
	@GetMapping("/obtener-config")
	public ResponseEntity<?> obtenerConfiguracion(@Value("${server.port}") String puerto){
		log.info(texto);
		Map<String,String> json=new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		if (env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}
		
		
		return new ResponseEntity<Map<String,String>>(json,HttpStatus.OK);
	}
	*/


}
