package com.queuecare.service;

import com.queuecare.dto.AdminDashboardSummaryDto;
import com.queuecare.dto.DoctorConsultationCountDto;
import com.queuecare.dto.DoctorDashboardTodayDto;
import com.queuecare.dto.DoctorQueueItemDto;
import com.queuecare.model.Patient;
import com.queuecare.model.QueueEntry;
import com.queuecare.model.enums.AppointmentStatus;
import com.queuecare.model.enums.QueueStatus;
import com.queuecare.repository.AppointmentRepository;
import com.queuecare.repository.QueueEntryRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final AppointmentRepository appointmentRepository;
    private final QueueEntryRepository queueEntryRepository;

    public DashboardService(
        AppointmentRepository appointmentRepository,
        QueueEntryRepository queueEntryRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.queueEntryRepository = queueEntryRepository;
    }

    @Transactional(readOnly = true)
    public AdminDashboardSummaryDto getAdminSummary() {
        LocalDate today = LocalDate.now();

        long totalAppointmentsToday = appointmentRepository.countByAppointmentDate(today);

        long totalPatientsToday = appointmentRepository
            .findByAppointmentDate(today)
            .stream()
            .map(a -> a.getPatient().getId())
            .distinct()
            .count();

        List<QueueEntry> todayEntries = queueEntryRepository.findByAppointmentAppointmentDate(today);

        double averageWaitingTime = calculateAverageWaitingTime(todayEntries);

        Map<Long, List<QueueEntry>> completedByDoctor = todayEntries.stream()
            .filter(e -> e.getStatus() == QueueStatus.COMPLETED)
            .collect(Collectors.groupingBy(e -> e.getAppointment().getDoctor().getId()));

        List<DoctorConsultationCountDto> doctorWise = completedByDoctor.entrySet().stream()
            .map(entry -> {
                Long doctorId = entry.getKey();
                QueueEntry sample = entry.getValue().get(0);
                String doctorName = sample.getAppointment().getDoctor().getUser().getName();
                long count = entry.getValue().size();
                return DoctorConsultationCountDto.builder()
                    .doctorId(doctorId)
                    .doctorName(doctorName)
                    .consultationsCount(count)
                    .build();
            })
            .collect(Collectors.toList());

        return AdminDashboardSummaryDto.builder()
            .totalPatientsToday(totalPatientsToday)
            .totalAppointmentsToday(totalAppointmentsToday)
            .averageWaitingTime(averageWaitingTime)
            .doctorWiseConsultationCount(doctorWise)
            .build();
    }

    @Transactional(readOnly = true)
    public DoctorDashboardTodayDto getDoctorTodayDashboard(Long doctorId) {
        LocalDate today = LocalDate.now();

        List<QueueEntry> todayQueue = queueEntryRepository
            .findByAppointmentDoctorIdAndAppointmentAppointmentDateOrderByQueueNumberAsc(
                doctorId,
                today
            );

        List<DoctorQueueItemDto> queueItems = todayQueue.stream()
            .map(entry -> {
                Patient patient = entry.getAppointment().getPatient();
                return DoctorQueueItemDto.builder()
                    .queueNumber(entry.getQueueNumber())
                    .patientName(patient.getName())
                    .status(entry.getStatus())
                    .build();
            })
            .collect(Collectors.toList());

        long completedConsultationsCount = queueEntryRepository
            .countByAppointmentAppointmentDateAndStatus(today, QueueStatus.COMPLETED);

        return DoctorDashboardTodayDto.builder()
            .todayQueue(queueItems)
            .completedConsultationsCount(completedConsultationsCount)
            .build();
    }

    private double calculateAverageWaitingTime(List<QueueEntry> entries) {
        if (entries.isEmpty()) {
            return 0.0;
        }

        OffsetDateTime now = OffsetDateTime.now();

        List<Long> waitingTimes = entries.stream()
            .filter(e -> e.getStatus() == QueueStatus.IN_CONSULTATION || e.getStatus() == QueueStatus.COMPLETED)
            .map(e -> Duration.between(e.getCreatedAt(), now).toMinutes())
            .collect(Collectors.toList());

        if (waitingTimes.isEmpty()) {
            return 0.0;
        }

        double sum = waitingTimes.stream().mapToLong(Long::longValue).sum();
        return sum / waitingTimes.size();
    }
}

