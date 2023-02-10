package com.example.integrate.spring.react.service;

import java.util.List;

import com.example.integrate.spring.react.model.Product;

public interface ProductService {

	public List<Product> prdts();

	public Product byId(int id);

	public Product addProduct(Product prd);

	public Product updateProduct(int id, Product prd);

	public Product deleteProduct(int id);

}
