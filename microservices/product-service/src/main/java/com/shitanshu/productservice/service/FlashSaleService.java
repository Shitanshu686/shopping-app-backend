package com.shitanshu.productservice.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.shitanshu.productservice.model.FlashSale;
import com.shitanshu.productservice.repository.FlashSaleRepository;

@Service
public class FlashSaleService {

    private final FlashSaleRepository flashSaleRepository;

    private static final long SALE_DURATION_HOURS = 12;

    public FlashSaleService(FlashSaleRepository flashSaleRepository) {
        this.flashSaleRepository = flashSaleRepository;
    }

    public FlashSale getActiveFlashSale() {

        LocalDateTime now = LocalDateTime.now();

        FlashSale flashSale = flashSaleRepository
                .findFirstByActiveTrueOrderByEndTimeAsc()
                .orElse(null);

        if (flashSale == null) {
            flashSale = new FlashSale(
                    now,
                    now.plusHours(SALE_DURATION_HOURS),
                    true
            );

            return flashSaleRepository.save(flashSale);
        }

        if (now.isBefore(flashSale.getEndTime())) {
            return flashSale;
        }

        LocalDateTime nextStart = flashSale.getEndTime();
        LocalDateTime nextEnd = nextStart.plusHours(SALE_DURATION_HOURS);

        while (!now.isBefore(nextEnd)) {
            nextStart = nextEnd;
            nextEnd = nextStart.plusHours(SALE_DURATION_HOURS);
        }

        flashSale.setStartTime(nextStart);
        flashSale.setEndTime(nextEnd);
        flashSale.setActive(true);

        return flashSaleRepository.save(flashSale);
    }
}
