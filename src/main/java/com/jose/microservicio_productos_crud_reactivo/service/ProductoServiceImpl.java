package com.jose.microservicio_productos_crud_reactivo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jose.microservicio_productos_crud_reactivo.model.Producto;
import com.jose.microservicio_productos_crud_reactivo.repository.ProductosRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoServiceImpl implements ProductoService{

    @Autowired
    ProductosRepository productosRepository;

    @Override
    public Flux<Producto> catalogo() {
        return productosRepository.findAll();
    }

    @Override
    public Flux<Producto> productoCategoria(String categoria) {
        return productosRepository.findByCategoria(categoria);
    }

    @Override
    public Mono<Producto> productoCodigo(int codigo) {
        return productosRepository.findById(codigo);
    }

    @Override
    public Mono<Producto> altaProducto(Producto producto) {
        return productoCodigo(producto.getCodigo()).switchIfEmpty(Mono.just(producto).flatMap(productoMap -> productosRepository.save(productoMap)));
    }

    @Override
    public Mono<Producto> eliminarProducto(int codigo) {
        return productoCodigo(codigo).flatMap(productoMap -> productosRepository.deleteById(codigo).then(Mono.just(productoMap)));
    }

    @Override
    public Mono<Producto> actualizarPrecio(int codigo, double precio) {
        return productoCodigo(codigo).flatMap(productoMap -> {
            productoMap.setPrecioUnitario(precio);
            return productosRepository.save(productoMap);
        });
    }

}
