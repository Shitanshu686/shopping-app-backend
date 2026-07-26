package com.shitanshu.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shitanshu.shopping.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

}