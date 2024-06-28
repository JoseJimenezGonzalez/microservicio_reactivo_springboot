package com.jose.microservicio_productos_crud_reactivo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.jose.microservicio_productos_crud_reactivo.model.Producto;
import com.jose.microservicio_productos_crud_reactivo.service.ProductoService;

/*@RestController
@RequestMapping("/api")*/
@Configuration
public class ProductoController {

    @Autowired
    ProductoService productoService;

    /*
    @GetMapping("/productos")
    public ResponseEntity<Flux<Producto>> productos(){
        return new ResponseEntity<>(productoService.catalogo(), HttpStatus.OK);
    }

    //Explicacion
    //productos -> Flux<Producto>
    //Cuando hacemos collectList() a productos me genera Mono<List<Producto>>
    //Mono<List<Producto>> encapsula a List<Producto> por lo que cuando se llama a map se obtiene  List<Product>

    @GetMapping(value = "/categoria")
    public Mono<ResponseEntity<?>> productosCategoria(@RequestParam("categoria") String categoria) {
        Flux<Producto> productos = productoService.productoCategoria(categoria);
        return productos.collectList().map(lista -> {
            if (lista.isEmpty()) {
                return new ResponseEntity<>("Esa categoria no existe en nuestra base de datos", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(lista, HttpStatus.OK);
            }
        });
    }

    @GetMapping(value = "/buscarPorCodigo")
    public Mono<ResponseEntity> productoCodigo(@RequestParam("codigo") int codigo) {
        Mono<Producto> productoMono = productoService.productoCodigo(codigo);
        return productoMono.map(producto -> ResponseEntity.status(HttpStatus.OK).body(producto))
        .cast(ResponseEntity.class)
        .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No existe ese producto en nuestra base de datos"));
    }


    //Revisar el alta de productos
    @PostMapping(value = "/alta", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<?>> altaProducto(@RequestBody Producto producto){
        producto.setEsNuevo(true);
        return new ResponseEntity<>(productoService.altaProducto(producto), HttpStatus.CREATED); 
    }

    @DeleteMapping(value = "/eliminar")
    public Mono<ResponseEntity<Producto>> eliminarProducto(@RequestParam("codigo") int codigo){
        return productoService.eliminarProducto(codigo)
        .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
        .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @PutMapping(value = "/actualizar")
    public Mono<ResponseEntity<Producto>> actualizarProducto(@RequestParam("codigo") int codigo, @RequestParam("precioUnitario") double precioUnitario){
        return productoService.actualizarPrecio(codigo, precioUnitario)
        .map(producto -> new ResponseEntity<>(producto, HttpStatus.OK))
        .switchIfEmpty(Mono.just(new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }
    */    

    //Revisar el de codigo
    @Bean
    public RouterFunction<ServerResponse> respuestas() {
        return RouterFunctions
            .route(RequestPredicates.GET("/api/productos"), 
                req -> ServerResponse.ok().body(productoService.catalogo(), Producto.class))
            .andRoute(RequestPredicates.GET("/api/productos/{categoria}"), 
                req -> ServerResponse.ok().body(productoService.productoCategoria(req.pathVariable("categoria")), Producto.class))
            .andRoute(RequestPredicates.GET("/api/productos/{codigo}"), 
                req -> ServerResponse.ok().body(productoService.productoCodigo(req.queryParam("codigo").map(string -> Integer.parseInt(string)).get()), Producto.class))
            .andRoute(RequestPredicates.POST("/api/productos/alta"), 
                req -> req.bodyToMono(Producto.class)//Mono<Producto>
                .flatMap(producto -> {
                    //Se mete porque espera que es un save no un alta
                    producto.setEsNuevo(true);
                    return productoService.altaProducto(producto);
                })
                .flatMap(v -> ServerResponse.ok().build()))
            .andRoute(RequestPredicates.DELETE("/api/productos/eliminar"), 
                req -> productoService.eliminarProducto(req.queryParam("codigo").map(string -> Integer.parseInt(string)).get())
                .flatMap(product -> ServerResponse.ok().bodyValue(product)).switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build()))
            .andRoute(RequestPredicates.PUT("/api/productos/actualizar"), 
                req -> productoService
                .actualizarPrecio(
                    req.queryParam("codigo").map(string -> Integer.parseInt(string)).get(), 
                    req.queryParam("precioUnitario").map(string -> Double.parseDouble(string)).get()
                ).flatMap(producto -> ServerResponse.ok().bodyValue(producto)));
    }

    @Bean
    CorsWebFilter corsFilter() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowCredentials(true);
	    config.addAllowedOrigin("*");
	    config.addAllowedHeader("*");
	    config.addAllowedMethod("*");
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);
	    return new CorsWebFilter(source);
    }

}
