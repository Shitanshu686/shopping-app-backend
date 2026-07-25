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
                "Allen Solly Men's Casual Shirt",
                "Allen Solly",
                "100% Cotton | Regular Fit | Full Sleeve | Blue",
                1499,
                2199,
                4.5,
                "images/shirt.jpg",
                "Fashion",
                35));

        products.add(new Product(
                2,
                "Noise ColorFit Pro 5",
                "Noise",
                "AMOLED Display | Bluetooth Calling | IP68 Water Resistant",
                3499,
                4999,
                4.4,
                "images/watch.jpg",
                "Watches",
                18));

        products.add(new Product(
                3,
                "Nike Revolution 7",
                "Nike",
                "Running Shoes | Lightweight | Black",
                3999,
                5499,
                4.7,
                "images/shoes.jpg",
                "Shoes",
                24));

        return products;
    }
}