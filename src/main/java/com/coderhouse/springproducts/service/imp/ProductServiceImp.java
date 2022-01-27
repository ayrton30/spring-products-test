package com.coderhouse.springproducts.service.imp;

import com.coderhouse.springproducts.exception.IdNotFoundException;
import com.coderhouse.springproducts.model.Product;
import com.coderhouse.springproducts.repository.ProductRepository;
import com.coderhouse.springproducts.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImp implements ProductService {

    private final ProductRepository repository;

    @Override
    public Product createProduct(Product producto) {
        return this.repository.save(producto);
    }

    @Override
    public Product getById(Long id) throws IdNotFoundException{
        return this.repository.findById(id).orElseThrow(() -> new IdNotFoundException());
    }

    @Override
    public List<Product> findByPriceGreaterThan(Integer price) {
        return this.repository.findByPriceGreaterThan(price);
    }

    @Override
    public Iterable<Product> getAllProducts() {
        return this.repository.findAll();
    }

    @Override
    public Product updateProduct(Product producto) throws IdNotFoundException {
        if(!this.repository.existsById(producto.getId())) {
            throw new IdNotFoundException();
        }
        return this.repository.save(producto);
    }

    @Override
    public void deleteProduct(Long id) throws IdNotFoundException {
        if(!this.repository.existsById(id)) {
            throw new IdNotFoundException();
        }
        this.repository.deleteById(id);
    }
}
