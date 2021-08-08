package com.payoneer.JobManagementSystem.service.impl;

import com.payoneer.JobManagementSystem.constants.JobStatus;
import com.payoneer.JobManagementSystem.controller.JobSchedulerController;
import com.payoneer.JobManagementSystem.exception.ElementNotFoundException;
import com.payoneer.JobManagementSystem.exception.JobManagementException;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.model.TriggerModel;
import com.payoneer.JobManagementSystem.repository.JobRepository;
import com.payoneer.JobManagementSystem.service.JobService;
import com.payoneer.JobManagementSystem.service.TriggerService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Component
@Slf4j
public class JobServiceImpl implements JobService {
    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);

    @Autowired
    JobRepository jobRepository;

    @Autowired
    TriggerService triggerService;


    @Override
    public ResponseEntity<String> save(JobModel jobModel) {
        logger.info("Saving/Updating job - JOB_ID" + jobModel.getId());
        //jobId is auto generated...expecting null from user
        if (null != jobModel.getId()) {
            return new ResponseEntity<>("Job creation failed, job Id must be left blank ", HttpStatus.BAD_REQUEST);
        }
        //validate trigger specified on requests
        if(!triggerValid(jobModel)){
            return new ResponseEntity<>("Job creation failed, trigger specified is not configured in database ", HttpStatus.BAD_REQUEST);
        }

        try {
            jobRepository.save(jobModel);
            logger.info("Saved/Updated job - JOB_ID" + jobModel.getId());
        } catch (Exception ex) {
            logger.error("Error occurred while saving the job " + ex.getStackTrace());
            throw new JobManagementException("Error while creating job" + ex.getMessage());
        }
        return new ResponseEntity<>("Job Created Successfully", HttpStatus.OK);
    }

    private boolean triggerValid(JobModel jobModel) {
        boolean result = false;
        if (jobModel.getTrigger() != null && jobModel.getTrigger().getId() != null) {
            TriggerModel trigger = triggerService.getTriggerById(jobModel.getTrigger().getId());
            if (trigger != null) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public JobModel getJobById(Long jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ElementNotFoundException("requested job does not exist"));
    }

    @Override
    public List<JobModel> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public ResponseEntity<String> deleteJobById(Long jobId) {
        JobModel deleteJob = jobRepository.findById(jobId).get();
        if(deleteJob == null){
            return new ResponseEntity<>("The job ID to be deleted does not exist.",HttpStatus.BAD_REQUEST);
        }
        if(deleteJob.getStatus().equalsIgnoreCase(JobStatus.RUNNING)){
            return new ResponseEntity<>("The job can't be deleted while it's being run.",HttpStatus.BAD_REQUEST);
        }
        else{
            try{
                jobRepository.deleteById(jobId);
            }catch (Exception ex){
                logger.error("Error occurred while deleting the job " + ex.getStackTrace());
                throw new JobManagementException("Error while deleting job, please try again ");
            }
        }
        return new ResponseEntity<>("The job has been successfully deleted.",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateJob(JobModel jobModel) {
        JobModel jobToModify = jobRepository.findById(jobModel.getId()).get();
        //cant update the job to RUNNING state as it'd be done by ONLY scheduler
        if(jobModel.getStatus().equalsIgnoreCase(JobStatus.RUNNING)){
            return new ResponseEntity<>("The job's state cant be change to 'RUNNING' manually.",HttpStatus.BAD_REQUEST);
        }
        if(jobToModify == null){
            return new ResponseEntity<>("The job ID to be updated does not exist.",HttpStatus.BAD_REQUEST);
        }
        if(jobToModify.getStatus().equalsIgnoreCase(JobStatus.RUNNING)){
            return new ResponseEntity<>("The job can't be updated while it's being run.",HttpStatus.BAD_REQUEST);
        }
        if(!triggerValid(jobModel)){
            return new ResponseEntity<>("Job updation failed, trigger specified is not configured in database ", HttpStatus.BAD_REQUEST);
        }
        try{
            jobRepository.save(jobModel);
        }catch (Exception ex){
            logger.error("Error occurred while updating the job " + ex.getStackTrace());
            throw new JobManagementException("Error while updating job, please try again ");
        }
        return new ResponseEntity<>("The job has been updated successfully.",HttpStatus.OK);
    }


}
