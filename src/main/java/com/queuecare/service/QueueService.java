package com.queuecare.service;

import com.queuecare.dto.QueueEntryResponse;
import com.queuecare.exception.BusinessException;
import com.queuecare.model.Appointment;
import com.queuecare.model.QueueEntry;
import com.queuecare.model.enums.AppointmentStatus;
import com.queuecare.model.enums.QueueStatus;
import com.queuecare.repository.QueueEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QueueService {

    private final QueueEntryRepository queueEntryRepository;

    public QueueService(QueueEntryRepository queueEntryRepository) {
        this.queueEntryRepository = queueEntryRepository;
    }

    @Transactional
    public void enqueueIfTodayAndBooked(Appointment appointment) {
        if (!Objects.equals(appointment.getStatus(), AppointmentStatus.BOOKED)) {
            return;
        }
        if (!Objects.equals(appointment.getAppointmentDate(), LocalDate.now())) {
            return;
        }
        if (queueEntryRepository.existsByAppointmentId(appointment.getId())) {
            return;
        }

        Long doctorId = appointment.getDoctor().getId();
        int maxQueue = queueEntryRepository.findMaxQueueNumberForDoctorAndDate(
            doctorId,
            appointment.getAppointmentDate()
        );
        int nextQueueNumber = maxQueue + 1;

        QueueEntry entry = QueueEntry.builder()
            .appointment(appointment)
            .queueNumber(nextQueueNumber)
            .status(QueueStatus.WAITING)
            .build();

        queueEntryRepository.save(entry);
    }

    @Transactional(readOnly = true)
    public List<QueueEntryResponse> getTodayQueueForDoctor(Long doctorId) {
        List<QueueEntry> entries = queueEntryRepository
            .findByAppointmentDoctorIdAndAppointmentAppointmentDateOrderByQueueNumberAsc(
                doctorId,
                LocalDate.now()
            );

        return entries.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public QueueEntryResponse callNextForDoctor(Long doctorId) {
        LocalDate today = LocalDate.now();

        queueEntryRepository
            .findFirstByAppointmentDoctorIdAndAppointmentAppointmentDateAndStatusOrderByQueueNumberAsc(
                doctorId,
                today,
                QueueStatus.IN_CONSULTATION
            )
            .ifPresent(current -> {
                current.setStatus(QueueStatus.COMPLETED);
                queueEntryRepository.save(current);
            });

        QueueEntry nextWaiting = queueEntryRepository
            .findFirstByAppointmentDoctorIdAndAppointmentAppointmentDateAndStatusOrderByQueueNumberAsc(
                doctorId,
                today,
                QueueStatus.WAITING
            )
            .orElseThrow(() -> new BusinessException("No waiting patients in queue"));

        nextWaiting.setStatus(QueueStatus.IN_CONSULTATION);
        QueueEntry saved = queueEntryRepository.save(nextWaiting);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<QueueEntryResponse> getTodayQueueStatusForPatient(Long patientId) {
        List<QueueEntry> entries = queueEntryRepository
            .findByAppointmentPatientIdAndAppointmentAppointmentDateOrderByQueueNumberAsc(
                patientId,
                LocalDate.now()
            );

        return entries.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<QueueEntryResponse> getTodayQueueForAllDoctors() {
        List<QueueEntry> entries = queueEntryRepository.findByAppointmentAppointmentDate(LocalDate.now());
        return entries.stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    private QueueEntryResponse toResponse(QueueEntry entry) {
        return QueueEntryResponse.builder()
            .id(entry.getId())
            .appointmentId(entry.getAppointment().getId())
            .doctorId(entry.getAppointment().getDoctor().getId())
            .patientId(entry.getAppointment().getPatient().getId())
            .queueNumber(entry.getQueueNumber())
            .status(entry.getStatus())
            .appointmentDate(entry.getAppointment().getAppointmentDate())
            .timeSlot(entry.getAppointment().getTimeSlot())
            .build();
    }
}

