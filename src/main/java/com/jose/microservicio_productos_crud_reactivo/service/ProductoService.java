package com.jose.microservicio_productos_crud_reactivo.service;

import com.jose.microservicio_productos_crud_reactivo.model.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {
    Flux<Producto> catalogo();
    Flux<Producto> productoCategoria(String categoria);
    Mono<Producto> productoCodigo(int codigo);
    Mono<Producto> altaProducto(Producto producto);
    Mono<Producto> eliminarProducto(int codigo);
    Mono<Producto> actualizarPrecio(int codigo, double precio);
}
