package com.shitanshu.shopping.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shitanshu.shopping.dto.AdminDashboardResponseDTO;
import com.shitanshu.shopping.dto.AdminRecentOrderDTO;
import com.shitanshu.shopping.model.Order;
import com.shitanshu.shopping.repository.OrderRepository;
import com.shitanshu.shopping.repository.ProductRepository;
import com.shitanshu.shopping.repository.UserRepository;

@Service
public class AdminDashboardService {

	private static final Logger log =
	        LoggerFactory.getLogger(AdminDashboardService.class);
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;


    // =========================
    // ADMIN DASHBOARD DATA
    // =========================

    public AdminDashboardResponseDTO getDashboardData() {
    	log.info("Loading admin dashboard data");
    	
        // =========================
        // BASIC DASHBOARD DATA
        // =========================
    	try {
    		
    		long totalProducts =
                productRepository.count();

        long totalUsers =
                userRepository.count();

        long totalOrders =
                orderRepository.count();
        log.debug("Total orders: {}", totalOrders);

        double totalRevenue =
                orderRepository.getTotalRevenue();


        // =========================
        // INVENTORY DATA
        // =========================

        long lowStockProducts =
                productRepository.countByStockBetween(
                        1,
                        10
                );

        long outOfStockProducts =
                productRepository.countByStock(
                        0
                );
        if (outOfStockProducts > 0) {

            log.warn(
                "There are {} out-of-stock products",
                outOfStockProducts
            );

        }

        // =========================
        // RECENT ORDERS
        // =========================

        List<Order> recentOrders =
                orderRepository.findRecentOrders();

        // =========================
        // CONVERT ORDERS TO DTO
        // =========================

        List<AdminRecentOrderDTO> recentOrderDTOs =
                recentOrders.stream()
                        .map(order ->
                                new AdminRecentOrderDTO(
                                        order.getId(),
                                        order.getUser().getName(),
                                        order.getTotalAmount(),
                                        order.getStatus(),
                                        order.getCreatedAt()
                                )
                        )
                        .toList();


        // =========================
        // DASHBOARD RESPONSE
        // =========================

        return new AdminDashboardResponseDTO(
                totalProducts,
                totalUsers,
                totalOrders,
                totalRevenue,
                lowStockProducts,
                outOfStockProducts,
                recentOrderDTOs
        );
    	}
    	catch (Exception e) {

            log.error(
                    "Failed to load admin dashboard data",
                    e
            );

            throw e;
        }
    }
}