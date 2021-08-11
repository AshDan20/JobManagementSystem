package com.payoneer.JobManagementSystem.job;

import com.payoneer.JobManagementSystem.constants.JobStatus;
import com.payoneer.JobManagementSystem.controller.JobSchedulerController;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.repository.JobRepository;
import com.payoneer.JobManagementSystem.service.impl.JobServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SendEmailJob extends JobServiceImpl implements Job {

    public SendEmailJob() {
    }

    @Autowired
    JobRepository jobRepository;

    private Long jobId;

    private static JobModel sendEmailJobModel;


    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);
    final String JOB_NAME = sendEmailJobModel.getJobname();

    public static void setJob(JobModel jobModel) {
        sendEmailJobModel = jobModel;
    }

    public static JobDetail buildSendEmailJobDetail(JobModel jobModel) {
        JobDataMap jobDataMap = new JobDataMap();
        return JobBuilder.newJob(SendEmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), jobModel.getJobgroup())
                .withDescription(jobModel.getJobname())
                //.usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("[jobName] : " + JOB_NAME + " : TRIGGERED");
        long jobRunTime = 1;
        //do something
        try {
            doJobTask(jobRunTime);
        } catch (Exception e) {
            logger.error("[jobName] : " +JOB_NAME + " : FAILED - [ERROR] :" + e.getMessage());
            sendEmailJobModel.setStatus(JobStatus.FAILED);
            jobRepository.save(sendEmailJobModel);
        }
        sendEmailJobModel.setStatus(JobStatus.SUCCESS);
        jobRepository.save(sendEmailJobModel);
        logger.info("[jobName] : " + JOB_NAME + " : COMPLETED");

    }

    private void doJobTask(long jobRunTime) throws InterruptedException {
        //change satus to RUNNING
        sendEmailJobModel.setStatus(JobStatus.RUNNING);
        jobRepository.save(sendEmailJobModel);
        logger.info("[jobName] : " +JOB_NAME + " : RUNNING....");
        TimeUnit.MILLISECONDS.sleep(60000); // let it for this time
     }

    public Long getJobId() {
        return this.jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public JobModel getJob() {
        return sendEmailJobModel;
    }


}
