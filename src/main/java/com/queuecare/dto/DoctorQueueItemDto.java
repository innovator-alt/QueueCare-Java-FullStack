package com.queuecare.dto;

import com.queuecare.model.enums.QueueStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorQueueItemDto {

    private Integer queueNumber;
    private String patientName;
    private QueueStatus status;
}

