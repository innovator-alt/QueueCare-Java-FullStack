package com.queuecare.dto;

import com.queuecare.model.enums.AppointmentStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppointmentResponse {

    private Long id;
    private Long patientId;
    private Long doctorId;
    private LocalDate appointmentDate;
    private String timeSlot;
    private AppointmentStatus status;
}

