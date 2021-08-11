package com.payoneer.JobManagementSystem.service;

import com.payoneer.JobManagementSystem.model.JobModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Ashish Dandekar
 *
 */
@Service

public interface JobService {

    ResponseEntity<String> save(JobModel jobModel);

    JobModel getJobById(Long jobId);

    List<JobModel> getAllJobs();

    ResponseEntity<String> deleteJobById(Long jobId);

    ResponseEntity<String> updateJob(JobModel jobStatus);
}
