package com.payoneer.JobManagementSystem.job;

import com.payoneer.JobManagementSystem.constants.JobStatus;
import com.payoneer.JobManagementSystem.controller.JobSchedulerController;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.repository.JobRepository;
import com.payoneer.JobManagementSystem.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author Ashish Dandekar
 * <p>
 * Job to refresh cache
 */
@Slf4j
public class RefreshCacheJob implements Job {

    private Long jobId;

    private static JobModel refreshCacheJob;
    @Autowired
    JobService jobService;

    @Autowired
    JobRepository jobRepository;

    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);
    final String JOB_NAME = refreshCacheJob.getJobname();

    public static void setJob(JobModel jobModel) {
        refreshCacheJob = jobModel;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("[jobName] : " + JOB_NAME + " : TRIGGERED");
        long jobRunTime = 30000;
        //do something
        try {
            doJobTask(jobRunTime);
        } catch (Exception e) {
            logger.error("[jobName] : " +JOB_NAME + " : FAILED - [ERROR] :" + e.getMessage());
            refreshCacheJob.setStatus(JobStatus.FAILED);
            jobRepository.save(refreshCacheJob);
        }
        refreshCacheJob.setStatus(JobStatus.SUCCESS);
        jobRepository.save(refreshCacheJob);
        logger.info("[jobName] : " +JOB_NAME + " : COMPLETED");

    }

    private void doJobTask(long jobRunTime) throws InterruptedException {
        //change satus to RUNNING
        refreshCacheJob.setStatus(JobStatus.RUNNING);
        jobRepository.save(refreshCacheJob);
        TimeUnit.MILLISECONDS.sleep(jobRunTime);
    }

    public Long getJobId() {
        return this.jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public JobModel getJob() {
        return refreshCacheJob;
    }


    public static JobDetail buildRefreshCacheJobDetail() {

        return JobBuilder.newJob(RefreshCacheJob.class)
                .withIdentity(UUID.randomUUID().toString(), "cache-group")
                .withDescription(refreshCacheJob.getJobname())
                //.usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

}
