package com.queuecare.controller;

import com.queuecare.dto.DoctorDashboardTodayDto;
import com.queuecare.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/doctor/dashboard")
public class DoctorDashboardController {

    private final DashboardService dashboardService;

    public DoctorDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/today")
    public ResponseEntity<DoctorDashboardTodayDto> getToday(
        @RequestParam Long doctorId
    ) {
        return ResponseEntity.ok(dashboardService.getDoctorTodayDashboard(doctorId));
    }
}

