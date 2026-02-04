package com.queuecare.controller;

import com.queuecare.dto.AppointmentResponse;
import com.queuecare.dto.PatientResponseDto;
import com.queuecare.dto.QueueEntryResponse;
import com.queuecare.dto.ReceptionAppointmentRequest;
import com.queuecare.dto.WalkInPatientRequest;
import com.queuecare.service.ReceptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reception")
public class ReceptionController {

    private final ReceptionService receptionService;

    public ReceptionController(ReceptionService receptionService) {
        this.receptionService = receptionService;
    }

    @PostMapping("/patient/walk-in")
    public ResponseEntity<PatientResponseDto> registerWalkIn(
        @Valid @RequestBody WalkInPatientRequest request
    ) {
        return ResponseEntity.ok(receptionService.registerWalkIn(request));
    }

    @PostMapping("/appointments")
    public ResponseEntity<AppointmentResponse> bookAppointment(
        @Valid @RequestBody ReceptionAppointmentRequest request
    ) {
        return ResponseEntity.ok(receptionService.bookAppointmentForWalkIn(request));
    }

    @GetMapping("/queue/today")
    public ResponseEntity<List<QueueEntryResponse>> getTodayQueue() {
        return ResponseEntity.ok(receptionService.getTodayQueueForAllDoctors());
    }
}

