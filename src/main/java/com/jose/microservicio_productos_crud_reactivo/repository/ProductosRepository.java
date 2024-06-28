package com.jose.microservicio_productos_crud_reactivo.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.jose.microservicio_productos_crud_reactivo.model.Producto;
import reactor.core.publisher.Flux;

public interface ProductosRepository extends ReactiveCrudRepository<Producto, Integer>{

    Flux<Producto> findByCategoria(String categoria);

    /*
    @Transactional
    @Modifying
    Mono<Void> deledeleteByNombre(String nombre);

    @Transactional
    @Modifying
    @Query(value = "DELETE from productos where precio > ?")
    Mono<Void> deletePrecio(double precioMax);
    */

}
