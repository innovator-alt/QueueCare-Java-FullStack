package com.queuecare.repository;

import com.queuecare.model.Appointment;
import com.queuecare.model.enums.AppointmentStatus;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    boolean existsByDoctorIdAndAppointmentDateAndTimeSlotAndStatus(
        Long doctorId,
        LocalDate appointmentDate,
        String timeSlot,
        AppointmentStatus status
    );

    long countByAppointmentDate(LocalDate appointmentDate);

    java.util.List<Appointment> findByAppointmentDate(LocalDate appointmentDate);
}

