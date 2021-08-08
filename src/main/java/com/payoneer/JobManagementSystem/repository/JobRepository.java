package com.payoneer.JobManagementSystem.repository;

import com.payoneer.JobManagementSystem.model.JobModel;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

public interface JobRepository extends JpaRepository<JobModel, Long> {
}
