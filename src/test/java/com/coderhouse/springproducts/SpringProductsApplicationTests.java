package com.coderhouse.springproducts;

import com.coderhouse.springproducts.model.Product;
import com.coderhouse.springproducts.repository.ProductRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringProductsApplicationTests {

	private static Logger logger = LogManager.getLogger(SpringProductsApplicationTests.class);
	private static long start;
	private static long end;

	private String url;
	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ProductRepository repository;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void init() {
		//Imprimir un mensaje antes que se ejecute cada uno de los tests
		logger.info("Test");
		this.url = String.format("http://localhost:%d/api/product", port);
	}

	//Imprimir un mensaje luego de la ejecución de todos los test indicando el tiempo en segundos
	//la duración de la ejecución de todos los test.
	@BeforeAll
	public static void startTest() {
		logger.info("Inicio de test!");
		start = System.nanoTime();
	}
	@AfterAll
	public static void endTest() {
		end = System.nanoTime();
		logger.info("Tiempo de ejecución: {} segundos", TimeUnit.NANOSECONDS.toSeconds(end - start));
	}


	//Realizar test de la integración hacia la API Rest de productos creando las pruebas usando TestRestTemplate.
	@Test
	public void getAllProducts() {
		logger.info("Ejecutando test: getAllProducts()");

		var uriTest = url;
		var productResult = this.restTemplate.getForObject(uriTest, List.class);

		Assert.notNull(productResult, "Lista de productos no nula");
		Assert.notEmpty(productResult, "Lista de productos con elementos");
		Assert.isTrue(productResult.size() == 3, "Tamaño de la lista es de 3");
	}

	@Test
	public void getProductById() {
		logger.info("Ejecutando test: getProductById()");

		var uriTest = String.format("%s%s", url, "/1");
		var productResult =
				this.restTemplate.getForObject(uriTest, Product.class);

		Assert.notNull(productResult, "Producto no nulo");
		Assert.isTrue(productResult.getId() == 1, "ID del producto OK");
		Assert.isTrue(productResult.getPrice().equals(5000), "Precio del producto OK");
	}

	@Test
	public void createProduct() {
		logger.info("Ejecutando test: createProduct()");

		var uriTest = url;
		var product = Product.builder().title("post_product").price(3000).build();

		var productResult =
				this.restTemplate.postForObject(uriTest, product, Product.class);

		Assert.notNull(productResult, "Producto no nulo");
		Assert.isTrue(productResult.getId() == 4, "ID del producto OK");
		Assert.isTrue(productResult.getPrice().equals(3000), "Precio del producto OK");
	}

	@Test
	public void updateProduct() {
		logger.info("Ejecutando test: updateProduct()");

		var uriTest = url;
		var product = Product.builder().id(1L).title("new_title").price(9000).build();

		this.restTemplate.put(uriTest, product, Product.class);

		Assert.isTrue(repository.existsById(1L), "ID del producto OK");
		var productRepo = repository.findById(1L).get();
		Assert.isTrue(productRepo.getPrice().equals(9000), "Precio del producto OK");
		Assert.isTrue(productRepo.getTitle().equals("new_title"), "Precio del producto OK");
	}

	@Test
	public void deleteProduct() {
		logger.info("Ejecutando test: deleteProduct()");

		var uriTest = String.format("%s%s", url, "/1");
		var count = repository.count();

		this.restTemplate.delete(uriTest);

		Assert.isTrue(!repository.existsById(1L), "ID del producto OK");
		Assert.isTrue(repository.count() == count-1L, "Precio del producto OK");
	}

	//Realizar test de la integración hacia la API Rest de productos creando las pruebas usando HttpRequest.
	@Test
	public void getAllProductsHttpRequestHeader() throws IOException {
		logger.info("Ejecutando test: getAllProductsHttpRequestHeader()");

		var uriTest = url;
		var headerAppJson = "application/json";

		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);
		var mimeType = ContentType
				.getOrDefault(httpResponse.getEntity()).getMimeType();
		Assert.isTrue(headerAppJson.equals(mimeType),
				"Header application/json OK");
	}

	@Test
	public void getAllProductsHttpRequestPayload() throws IOException {
		logger.info("Ejecutando test: getAllProductsHttpRequestPayload()");

		var uriTest = url;
		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);

		String content = EntityUtils.toString(httpResponse.getEntity());
		var productResult = objectMapper.readValue(content, List.class);

		Assert.notNull(productResult, "Lista de productos no nula");
		Assert.notEmpty(productResult, "Lista de productos con elementos");
		Assert.isTrue(productResult.size() == 3, "Tamaño de la lista es de 3");
	}

	@Test
	public void getProductHttpRequestHeader() throws IOException {
		logger.info("Ejecutando test: getProductHttpRequestHeader()");

		var uriTest = String.format("%s%s", url, "/2");
		var headerAppJson = "application/json";

		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);
		var mimeType = ContentType
				.getOrDefault(httpResponse.getEntity()).getMimeType();
		Assert.isTrue(headerAppJson.equals(mimeType),
				"Header application/json OK");
	}

	@Test
	public void getProductHttpRequestPayload() throws IOException {
		logger.info("Ejecutando test: getProductHttpRequestPayload()");

		var uriTest = String.format("%s%s", url, "/2");
		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);

		String content = EntityUtils.toString(httpResponse.getEntity());
		var productResult = objectMapper.readValue(content, Product.class);

		Assert.notNull(productResult, "Producto no nulo");
		Assert.isTrue(productResult.getId() == 2, "ID del producto OK");
		Assert.isTrue(productResult.getPrice().equals(2000), "Precio del producto OK");
	}
}
