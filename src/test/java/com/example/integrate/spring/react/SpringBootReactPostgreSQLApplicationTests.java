package com.example.integrate.spring.react;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.deleteRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.exactly;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.putRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;
import org.json.JSONException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.ResponseEntity;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;


import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.integrate.spring.react.exception.ProductApiException;
import com.example.integrate.spring.react.model.Product;
import com.example.integrate.spring.react.service.impl.ProductRestClient;
import com.example.integrate.spring.react.controller.ProductController;

import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.http.Fault;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.WireMockServer;

import org.springframework.web.client.RestTemplate;

@SpringBootTest
class SpringBootReactPostgreSQLApplicationTests {

	@Test
	void contextLoads() {
	}

	private static final int HTTP_SERVICE_PORT = 8080;
	private static final int HTTPS_SERVICE_PORT = 9999;

	private List<Product> ALL_PRODUCTS = new ArrayList<>();

	private String baseURI;

	private ProductRestClient prdtRestClient;

	private static int idCounter = 1;

        private static long productId = 0;

	private static Product buildProduct(String code, String name, String pricehrk, String description, boolean isavailable) {
		Product prdt = new Product();
		prdt.setCode(code);
		prdt.setName(name);
		prdt.setPricehrk(pricehrk);
		prdt.setDescription(description);
		prdt.setIsAvailable(isavailable);

		idCounter++;

		return prdt;
	}

	@BeforeEach
	public void initializeProducts() {
		Product prdt1 = buildProduct("1111111111", "Braun Series 1", "500.00", "Trimer", true);
		Product prdt2 = buildProduct("2222222222", "Braun Series 3", "1500.00", "Trimer", true);
		Product prdt3 = buildProduct("3333333333", "Braun Series 5", "2500.00", "Trimer", true);
		Product prdt4 = buildProduct("4444444444", "Braun Series 7", "3500.00", "Trimer", true);
		Product prdt5 = buildProduct("5555555555", "Braun Series 9", "4500.00", "Trimer", true);
		Product prdt6 = buildProduct("6666666666", "Braun Series 9 PRO", "5500.00", "Trimer", true);

		ALL_PRODUCTS.addAll(Arrays.asList(prdt1, prdt2, prdt3, prdt4, prdt5, prdt6));

	}

        WireMockServer wireMockServer = new WireMockServer();


	@BeforeEach
	public void TestProductUsingWireMockAndJunit5() {
        try {
        String token1="";
        File fileOne = new File("eur_conversion");
	  File fileOne1 = new File("target/classes/static/eur_conversion");
        token1 = "7,53450";
        token1 = token1.replace("\r\n","");
	  token1 = token1.replace(",",".");
        PrintWriter writer = new PrintWriter(fileOne);
        writer.println(token1);
        writer.close();
        PrintWriter writer1 = new PrintWriter(fileOne1);
        writer1.println(token1);
        writer1.close();
        } catch (IOException e) {
     	  e.printStackTrace();
    	  }

		this.wireMockServer = new WireMockServer(
                      options().port(HTTP_SERVICE_PORT).httpsPort(HTTPS_SERVICE_PORT)
				.notifier(new ConsoleNotifier(true)).extensions(new ResponseTemplateTransformer(true)));

            this.wireMockServer.start();

		prdtRestClient = new ProductRestClient();

		prdtRestClient.createtableifnotexists();

		Product prdt = new Product();
		prdt.setCode("1111111111");
		prdt.setName("Braun Series 1");
		prdt.setPricehrk("500.00");
		prdt.setDescription("Trimer");
		prdt.setIsAvailable(true);

		this.wireMockServer.stubFor(post(urlPathEqualTo("http://localhost:8080/api/products")).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(Json.write(prdt))));


                Product newProduct = prdtRestClient.addProduct(prdt);

                productId = newProduct.getId();

		assertEquals(newProduct.getId(), productId);
		assertEquals(newProduct.getCode(), "1111111111");
		assertEquals(newProduct.getName(), "Braun Series 1");
		assertEquals(newProduct.getPricehrk(), "500.00");
		assertEquals(newProduct.getDescription(), "Trimer");
		assertEquals(newProduct.getIsAvailable(), true);

		this.wireMockServer.stubFor(get(urlPathMatching("http://localhost:8080/api/products")).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(Json.write(prdt))));

		List<Product> resultFromService = prdtRestClient.prdts();

		this.wireMockServer.stubFor(get(urlPathMatching("http://localhost:8080/api/products/"+productId)).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(Json.write(prdt))));

		Product resultPrdt = prdtRestClient.byId((int) productId);

		assertEquals(productId, resultPrdt.getId());
		assertEquals("1111111111", resultPrdt.getCode());
		assertEquals("Braun Series 1", resultPrdt.getName());
		assertEquals("Trimer", resultPrdt.getDescription());
		assertEquals(true, resultPrdt.getIsAvailable());

		long prdtId = productId;

                this.wireMockServer.stubFor(put(urlPathEqualTo("http://localhost:8080/api/products/" + prdtId)).willReturn(aResponse().withStatus(200)
				.withHeader("Content-Type", "application/json").withBodyFile("updateProductTemplate.json")));

		Product updateProductA = prdtRestClient.updateProduct((int) prdtId, prdt);

		assertEquals(updateProductA.getCode(), "1111111111");
		assertEquals(updateProductA.getName(), "Braun Series 1");
		assertEquals(updateProductA.getPricehrk(), "500.00");
		assertEquals(updateProductA.getDescription(), "Trimer");
		assertEquals(updateProductA.getIsAvailable(), true);

		this.wireMockServer.stubFor(delete(urlPathEqualTo("http://localhost:8080/api/products/" + prdtId)).willReturn(
				aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(Json.write(prdt))));

		Product deletedProduct = prdtRestClient.deleteProduct((int) prdtId);

		assertEquals(deletedProduct.getCode(), "1111111111");
		assertEquals(deletedProduct.getName(), "Braun Series 1");
		assertEquals(deletedProduct.getPricehrk(), "500.00");
		assertEquals(deletedProduct.getDescription(), "Trimer");
		assertEquals(deletedProduct.getIsAvailable(), true);
	}
}
