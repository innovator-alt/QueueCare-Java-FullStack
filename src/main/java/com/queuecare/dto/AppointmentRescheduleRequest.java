package com.queuecare.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentRescheduleRequest {

    @NotNull
    @FutureOrPresent
    private LocalDate appointmentDate;

    @NotBlank
    private String timeSlot;
}

