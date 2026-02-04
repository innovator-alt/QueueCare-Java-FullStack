package com.queuecare.repository;

import com.queuecare.model.QueueEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QueueEntryRepository extends JpaRepository<QueueEntry, Long> {
}

