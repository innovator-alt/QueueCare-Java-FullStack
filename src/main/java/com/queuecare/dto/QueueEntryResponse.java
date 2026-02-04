package com.queuecare.dto;

import com.queuecare.model.enums.QueueStatus;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QueueEntryResponse {

    private Long id;
    private Long appointmentId;
    private Long doctorId;
    private Long patientId;
    private Integer queueNumber;
    private QueueStatus status;
    private LocalDate appointmentDate;
    private String timeSlot;
}

