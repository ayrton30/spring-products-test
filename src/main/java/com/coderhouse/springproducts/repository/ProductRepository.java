package com.coderhouse.springproducts.repository;

import com.coderhouse.springproducts.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByPriceGreaterThan(Integer price);
}
