package com.coderhouse.springproducts.controller;

import com.coderhouse.springproducts.exception.IdNotFoundException;
import com.coderhouse.springproducts.model.Product;
import com.coderhouse.springproducts.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService service;

    @GetMapping("")
    public Iterable<Product> getProductos() {
        log.info("GET Request getProductos()");
        return this.service.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProducto(@PathVariable Long id) throws IdNotFoundException {
        log.info("GET Request getProducto(id)");
        return this.service.getById(id);
    }

    @GetMapping("/price/{price}")
    public List<Product> getProductosByPrice(@PathVariable Integer price) {
        log.info("GET Request getProductosByPrice(Integer price)");
        return this.service.findByPriceGreaterThan(price);
    }

    @PostMapping("")
    public Product insertProducto(@RequestBody Product producto) {
        log.info("POST Request insertProducto(Producto producto)");
        return this.service.createProduct(producto);
    }

    @PutMapping("")
    public Product updateProducto(@RequestBody Product producto) throws IdNotFoundException {
        log.info("PUT Request updateProducto(Producto producto)");
        return this.service.updateProduct(producto);
    }

    @DeleteMapping("/{id}")
    public void deleteProducto(@PathVariable Long id) throws IdNotFoundException {
        log.info("DELETE Request deleteProducto(Long id)");
        this.service.deleteProduct(id);
    }
}
