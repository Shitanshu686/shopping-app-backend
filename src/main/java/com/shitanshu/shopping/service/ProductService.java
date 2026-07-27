package com.shitanshu.shopping.service;
import com.shitanshu.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shitanshu.shopping.model.Product;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	
	
    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        
        return productRepository.findAll();
    }
}