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
        System.out.println("========== METHOD CALLED ==========");
    	System.out.println(productRepository.findAll());
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
        products.add(new Product(
        	    4,
        	    "Safari Marble Print Cabin Trolley",
        	    "Safari",
        	    "55 cm Cabin Luggage | Polycarbonate Hard Shell | 360° Spinner Wheels",
        	    3499,
        	    4999,
        	    4.5,
        	    "images/products/bags/BAG_1.jpg",
        	    
        	    "Luggage",
        	    18
        	));

        	products.add(new Product(
        	    5,
        	    "Aristocrat Printed Travel Trolley",
        	    "Aristocrat",
        	    "Medium Check-in Suitcase | Lightweight | TSA Lock",
        	    4299,
        	    5899,
        	    4.4,
        	    "images/products/bags/BAG_2.jpg",
        	    "Luggage",
        	    14
        	));

        	products.add(new Product(
        	    6,
        	    "American Tourister Pink Cabin Suitcase",
        	    "American Tourister",
        	    "Cabin Size Hard Luggage | Scratch Resistant | Spinner Wheels",
        	    3999,
        	    5599,
        	    4.7,
        	    "images/products/bags/BAG_3.jpg",
        	    "Luggage",
        	    20
        	));

        	products.add(new Product(
        	    7,
        	    "VIP Heritage Trolley Set",
        	    "VIP",
        	    "3 Piece Premium Luggage Set | Durable Polycarbonate | Spinner Wheels",
        	    10999,
        	    13999,
        	    4.8,
        	    "images/products/bags/BAG_4.jpg",
        	    "Luggage",
        	    8
        	));

        	products.add(new Product(
        	    8,
        	    "Samsonite Executive Cabin Trolley",
        	    "Samsonite",
        	    "Business Cabin Suitcase | Premium Leather Finish | 360° Spinner Wheels",
        	    8499,
        	    10999,
        	    4.9,
        	    "images/products/bags/BAG_5.jpg",
        	    "Luggage",
        	    10
        	));
        	products.add(new Product(
        		    9,
        		    "OnePlus Ace 5 Pro",
        		    "OnePlus",
        		    "12GB RAM | 256GB Storage | Snapdragon 8 Elite | AMOLED Display",
        		    49999,
        		    54999,
        		    4.8,
        		    "images/products/smartphones/SmartPhone_1.jpg",
        		    "Smartphones",
        		    18
        		));

        		products.add(new Product(
        		    10,
        		    "Xiaomi 15",
        		    "Xiaomi",
        		    "12GB RAM | 256GB Storage | Leica Camera | Snapdragon 8 Elite",
        		    52999,
        		    57999,
        		    4.7,
        		    "images/products/smartphones/SmartPhone_2.jpg",
        		    "Smartphones",
        		    15
        		));

        		products.add(new Product(
        		    11,
        		    "OnePlus 15",
        		    "OnePlus",
        		    "16GB RAM | 512GB Storage | 120Hz AMOLED | 5G",
        		    62999,
        		    67999,
        		    4.9,
        		    "images/products/smartphones/SmartPhone_3.jpg",
        		    "Smartphones",
        		    10
        		));

        		products.add(new Product(
        		    12,
        		    "Google Pixel 10",
        		    "Google",
        		    "12GB RAM | 256GB Storage | Tensor G5 Processor",
        		    69999,
        		    74999,
        		    4.8,
        		    "images/products/smartphones/SmartPhone_4.jpg",
        		    "Smartphones",
        		    12
        		));

        		products.add(new Product(
        		    13,
        		    "i17 Pro Max",
        		    "i17",
        		    "8GB RAM | 256GB Storage | Android 15 | 8000mAh Battery",
        		    18999,
        		    22999,
        		    4.3,
        		    "images/products/smartphones/SmartPhone_5.jpg",
        		    "Smartphones",
        		    25
        		));
        return products;
    }
}