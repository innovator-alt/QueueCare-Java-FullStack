package com.queuecare.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorConsultationCountDto {

    private Long doctorId;
    private String doctorName;
    private long consultationsCount;
}

