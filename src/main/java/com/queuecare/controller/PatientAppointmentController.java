package com.queuecare.controller;

import com.queuecare.dto.AppointmentRequest;
import com.queuecare.dto.AppointmentRescheduleRequest;
import com.queuecare.dto.AppointmentResponse;
import com.queuecare.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patient/appointments")
public class PatientAppointmentController {

    private final AppointmentService appointmentService;

    public PatientAppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    public ResponseEntity<AppointmentResponse> bookAppointment(
        @Valid @RequestBody AppointmentRequest request
    ) {
        return ResponseEntity.ok(appointmentService.bookAppointment(request));
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
        @PathVariable Long id,
        @Valid @RequestBody AppointmentRescheduleRequest request
    ) {
        return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
        appointmentService.cancelAppointment(id);
        return ResponseEntity.noContent().build();
    }
}

