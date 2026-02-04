package com.queuecare.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminDashboardSummaryDto {

    private long totalPatientsToday;
    private long totalAppointmentsToday;
    private double averageWaitingTime;
    private List<DoctorConsultationCountDto> doctorWiseConsultationCount;
}

