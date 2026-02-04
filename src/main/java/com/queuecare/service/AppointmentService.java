package com.queuecare.service;

import com.queuecare.dto.AppointmentRequest;
import com.queuecare.dto.AppointmentRescheduleRequest;
import com.queuecare.dto.AppointmentResponse;
import com.queuecare.exception.BusinessException;
import com.queuecare.exception.ResourceNotFoundException;
import com.queuecare.model.Appointment;
import com.queuecare.model.Doctor;
import com.queuecare.model.Patient;
import com.queuecare.model.enums.AppointmentStatus;
import com.queuecare.repository.AppointmentRepository;
import com.queuecare.repository.DoctorRepository;
import com.queuecare.repository.PatientRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(
        AppointmentRepository appointmentRepository,
        PatientRepository patientRepository,
        DoctorRepository doctorRepository
    ) {
        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        Patient patient = patientRepository.findById(request.getPatientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
            .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlotAndStatus(
            request.getDoctorId(),
            request.getAppointmentDate(),
            request.getTimeSlot(),
            AppointmentStatus.BOOKED
        );

        if (exists) {
            throw new BusinessException("Selected time slot is already booked for this doctor");
        }

        Appointment appointment = Appointment.builder()
            .patient(patient)
            .doctor(doctor)
            .appointmentDate(request.getAppointmentDate())
            .timeSlot(request.getTimeSlot())
            .status(AppointmentStatus.BOOKED)
            .build();

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Transactional
    public AppointmentResponse rescheduleAppointment(Long id, AppointmentRescheduleRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!Objects.equals(appointment.getStatus(), AppointmentStatus.BOOKED)) {
            throw new BusinessException("Only BOOKED appointments can be rescheduled");
        }

        boolean exists = appointmentRepository.existsByDoctorIdAndAppointmentDateAndTimeSlotAndStatus(
            appointment.getDoctor().getId(),
            request.getAppointmentDate(),
            request.getTimeSlot(),
            AppointmentStatus.BOOKED
        );

        if (exists) {
            throw new BusinessException("Selected time slot is already booked for this doctor");
        }

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setTimeSlot(request.getTimeSlot());

        Appointment saved = appointmentRepository.save(appointment);
        return toResponse(saved);
    }

    @Transactional
    public void cancelAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        if (!Objects.equals(appointment.getStatus(), AppointmentStatus.BOOKED)) {
            throw new BusinessException("Only BOOKED appointments can be cancelled");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
            .id(appointment.getId())
            .patientId(appointment.getPatient().getId())
            .doctorId(appointment.getDoctor().getId())
            .appointmentDate(appointment.getAppointmentDate())
            .timeSlot(appointment.getTimeSlot())
            .status(appointment.getStatus())
            .build();
    }
}

