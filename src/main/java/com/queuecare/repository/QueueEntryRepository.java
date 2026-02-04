package com.queuecare.repository;

import com.queuecare.model.QueueEntry;
import com.queuecare.model.enums.QueueStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {

    boolean existsByAppointmentId(Long appointmentId);

    @Query("select coalesce(max(q.queueNumber), 0) from QueueEntry q " +
           "where q.appointment.doctor.id = :doctorId " +
           "and q.appointment.appointmentDate = :date")
    int findMaxQueueNumberForDoctorAndDate(@Param("doctorId") Long doctorId,
                                           @Param("date") LocalDate date);

    List<QueueEntry> findByAppointmentDoctorIdAndAppointmentAppointmentDateOrderByQueueNumberAsc(
        Long doctorId,
        LocalDate appointmentDate
    );

    List<QueueEntry> findByAppointmentPatientIdAndAppointmentAppointmentDateOrderByQueueNumberAsc(
        Long patientId,
        LocalDate appointmentDate
    );

    Optional<QueueEntry> findFirstByAppointmentDoctorIdAndAppointmentAppointmentDateAndStatusOrderByQueueNumberAsc(
        Long doctorId,
        LocalDate appointmentDate,
        QueueStatus status
    );

    List<QueueEntry> findByAppointmentAppointmentDate(LocalDate appointmentDate);

    long countByAppointmentAppointmentDateAndStatus(LocalDate appointmentDate, QueueStatus status);
}

