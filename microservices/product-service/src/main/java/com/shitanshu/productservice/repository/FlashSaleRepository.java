package com.shitanshu.productservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shitanshu.productservice.model.FlashSale;

public interface FlashSaleRepository extends JpaRepository<FlashSale, Integer> {

    Optional<FlashSale> findFirstByActiveTrueOrderByEndTimeAsc();
}
