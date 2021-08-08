package com.payoneer.JobManagementSystem.repository;

import com.payoneer.JobManagementSystem.model.TriggerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TriggerRepository extends JpaRepository<TriggerModel, Long> {
}
