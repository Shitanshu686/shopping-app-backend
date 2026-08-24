package com.shitanshu.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shitanshu.shopping.dto.AdminDashboardResponseDTO;
import com.shitanshu.shopping.service.AdminDashboardService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminDashboardController {

    @Autowired
    private AdminDashboardService adminDashboardService;


    // =========================
    // ADMIN DASHBOARD
    // =========================

    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardResponseDTO> getDashboard() {

        AdminDashboardResponseDTO dashboard =
                adminDashboardService.getDashboardData();

        return ResponseEntity.ok(
                dashboard
        );
    }

}