package com.shitanshu.shopping.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.shitanshu.shopping.model.Product;

@Service
public class ProductService {

    public List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        products.add(new Product(
                1,
                "Shirt",
                "Cotton Casual Shirt",
                500,
                "images/shirt.jpg",
                "Clothing",
                20));

        products.add(new Product(
                2,
                "Watch",
                "Smart Watch",
                800,
                "images/watch.jpg",
                "Accessories",
                15));

        products.add(new Product(
                3,
                "Shoes",
                "Running Shoes",
                1200,
                "images/shoes.jpg",
                "Footwear",
                10));

        return products;
    }
}