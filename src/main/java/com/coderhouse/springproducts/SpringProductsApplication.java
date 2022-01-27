package com.coderhouse.springproducts;

import com.coderhouse.springproducts.model.Product;
import com.coderhouse.springproducts.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringProductsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProductsApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(ProductRepository repository) {
		return (args) -> {
			repository.save(Product.builder().title("product1").price(5000).build());
			repository.save(Product.builder().title("product2").price(2000).build());
			repository.save(Product.builder().title("product3").price(3000).build());
		};
	}
}
