package com.payoneer.JobManagementSystem.controller;

import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    JobService jobService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<JobModel> getAllJobs(){
        return jobService.getAllJobs();
    }

    @GetMapping
    @RequestMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public JobModel getJobById(@PathVariable ("id") long jobId){
        return jobService.getJobById(jobId);
    }

    @PostMapping
    public ResponseEntity<String> createJob(@RequestBody JobModel jobModel){
        return jobService.save(jobModel);
    }

    @DeleteMapping
    @RequestMapping(value = "/deleteJob/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable ("id") long jobId){
        return jobService.deleteJobById(jobId);
    }

    @PutMapping
    public ResponseEntity<String> updateJob(@RequestBody JobModel jobModel){
        return jobService.updateJob(jobModel);
    }

}
