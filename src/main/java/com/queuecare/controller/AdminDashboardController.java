package com.queuecare.controller;

import com.queuecare.dto.AdminDashboardSummaryDto;
import com.queuecare.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
public class AdminDashboardController {

    private final DashboardService dashboardService;

    public AdminDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    public ResponseEntity<AdminDashboardSummaryDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getAdminSummary());
    }
}

