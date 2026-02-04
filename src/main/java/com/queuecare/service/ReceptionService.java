package com.queuecare.service;

import com.queuecare.dto.AppointmentResponse;
import com.queuecare.dto.PatientResponseDto;
import com.queuecare.dto.ReceptionAppointmentRequest;
import com.queuecare.dto.WalkInPatientRequest;
import com.queuecare.dto.QueueEntryResponse;
import com.queuecare.exception.ResourceNotFoundException;
import com.queuecare.model.Patient;
import com.queuecare.service.AppointmentService;
import com.queuecare.service.QueueService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReceptionService {

    private final AppointmentService appointmentService;
    private final QueueService queueService;
    private final com.queuecare.repository.PatientRepository patientRepository;

    public ReceptionService(
        AppointmentService appointmentService,
        QueueService queueService,
        com.queuecare.repository.PatientRepository patientRepository
    ) {
        this.appointmentService = appointmentService;
        this.queueService = queueService;
        this.patientRepository = patientRepository;
    }

    @Transactional
    public PatientResponseDto registerWalkIn(WalkInPatientRequest request) {
        Patient patient = Patient.builder()
            .name(request.getName())
            .phone(request.getPhone())
            .email(request.getEmail())
            .build();

        Patient saved = patientRepository.save(patient);

        return PatientResponseDto.builder()
            .id(saved.getId())
            .name(saved.getName())
            .phone(saved.getPhone())
            .email(saved.getEmail())
            .build();
    }

    @Transactional
    public AppointmentResponse bookAppointmentForWalkIn(ReceptionAppointmentRequest request) {
        com.queuecare.dto.AppointmentRequest appointmentRequest = new com.queuecare.dto.AppointmentRequest();
        appointmentRequest.setPatientId(request.getPatientId());
        appointmentRequest.setDoctorId(request.getDoctorId());
        appointmentRequest.setAppointmentDate(request.getAppointmentDate());
        appointmentRequest.setTimeSlot(request.getTimeSlot());

        return appointmentService.bookAppointment(appointmentRequest);
    }

    @Transactional(readOnly = true)
    public List<QueueEntryResponse> getTodayQueueForAllDoctors() {
        return queueService.getTodayQueueForAllDoctors();
    }
}

