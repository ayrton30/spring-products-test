package com.coderhouse.springproducts.service;

import com.coderhouse.springproducts.exception.IdNotFoundException;
import com.coderhouse.springproducts.model.Product;

import java.util.List;

public interface ProductService {

    Product createProduct(Product producto);
    Product getById(Long id) throws IdNotFoundException;
    List<Product> findByPriceGreaterThan(Integer price);
    Iterable<Product> getAllProducts();
    Product updateProduct(Product producto) throws IdNotFoundException;
    void deleteProduct(Long id) throws IdNotFoundException;
}
