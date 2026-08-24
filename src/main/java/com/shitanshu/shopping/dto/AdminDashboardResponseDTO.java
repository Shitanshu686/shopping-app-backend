package com.shitanshu.shopping.dto;

import java.util.List;

public class AdminDashboardResponseDTO {

    private long totalProducts;

    private long totalUsers;

    private long totalOrders;

    private double totalRevenue;

    private long lowStockProducts;

    private long outOfStockProducts;

    private List<AdminRecentOrderDTO> recentOrders;


    public AdminDashboardResponseDTO() {
    }


    public AdminDashboardResponseDTO(
            long totalProducts,
            long totalUsers,
            long totalOrders,
            double totalRevenue,
            long lowStockProducts,
            long outOfStockProducts,
            List<AdminRecentOrderDTO> recentOrders) {

        this.totalProducts = totalProducts;

        this.totalUsers = totalUsers;

        this.totalOrders = totalOrders;

        this.totalRevenue = totalRevenue;

        this.lowStockProducts = lowStockProducts;

        this.outOfStockProducts = outOfStockProducts;

        this.recentOrders = recentOrders;
    }


    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }


    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }


    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }


    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }


    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public void setLowStockProducts(long lowStockProducts) {
        this.lowStockProducts = lowStockProducts;
    }


    public long getOutOfStockProducts() {
        return outOfStockProducts;
    }

    public void setOutOfStockProducts(long outOfStockProducts) {
        this.outOfStockProducts = outOfStockProducts;
    }


    public List<AdminRecentOrderDTO> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(
            List<AdminRecentOrderDTO> recentOrders) {

        this.recentOrders = recentOrders;
    }

}