package com.queuecare.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DoctorDashboardTodayDto {

    private List<DoctorQueueItemDto> todayQueue;
    private long completedConsultationsCount;
}

