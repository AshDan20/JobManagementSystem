package com.payoneer.JobManagementSystem.controller;

import com.payoneer.JobManagementSystem.constants.ClassNameConstants;
import com.payoneer.JobManagementSystem.constants.JobStatus;
import com.payoneer.JobManagementSystem.constants.LoggerConstants;
import com.payoneer.JobManagementSystem.exception.JobScheduleException;
import com.payoneer.JobManagementSystem.job.RefreshCacheJob;
import com.payoneer.JobManagementSystem.job.SendEmailJob;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ashish Dandekar
 * Controller class which handles job scheduling requests, returns jobs in specific state
 *
 */
@Slf4j
@RestController
@RequestMapping("/scheduler")
public class JobSchedulerController {


    @Autowired
    @Lazy
    JobService jobService;

    @Autowired
    private Scheduler scheduler;

    String jobName = null;
    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerController.class);
    JobDetail jobDetail = null;

    private static final List<String> validStatusList = new ArrayList<>(Arrays.asList("RUNNING","FAILED","QUEUED","SUCCESS"));

    /**
     * API method to schedule the job ... the trigger is fetched from DB which has its cron stored in DB
     * e.g. http://localhost:8080/scheduler/7
     * @param id
     * @throws SchedulerException
     */
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void scheduleJob(@PathVariable("id") long id) throws SchedulerException {

        JobModel jobModel = jobService.getJobById(id);
        if (jobModel == null) {
            logger.error(LoggerConstants.JOB_NOT_EXISTS + " -" + id);
            new JobScheduleException(LoggerConstants.JOB_NOT_EXISTS + " -" + id);
        }
        if (jobModel != null) {
            jobName = jobModel.getJobname();
        }
        try {
            if (jobName.equalsIgnoreCase(ClassNameConstants.SEND_EMAIL_JOB)) {
                SendEmailJob.setJob(jobModel);
                jobDetail = SendEmailJob.buildSendEmailJobDetail(jobModel);
            }
            else if(jobName.equalsIgnoreCase(ClassNameConstants.REFRESH_CACHE_JOB)){
                RefreshCacheJob.setJob(jobModel);
                jobDetail = RefreshCacheJob.buildRefreshCacheJobDetail(jobModel);
            }
        } catch (Exception e) {
            logger.error("Exception while building jobDetail for job -" + jobName);
            throw new JobScheduleException("Exception while building jobDetail "+e.getStackTrace());
        }
        Trigger trigger = buildJobTrigger(jobDetail, jobModel);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            logger.info("SCHEDULED - [jobName]:" + jobName + " [groupName]:"
                    + jobModel.getJobgroup() + " [NextFireTime]:" + trigger.getNextFireTime());
        } catch (Exception e) {
            logger.error("Exception while scheduling job -" + jobName);
            throw new JobScheduleException(" Exception while scheduling job "+e.getStackTrace());
        }
        scheduler.start();
    }

    /**
     * API method to return job names in particular status
     * e.g. http://localhost:8080/scheduler/jobs?status=RUNNING
     * @param status
     * @return
     * @throws SchedulerException
     */
    @GetMapping("/jobs")
    @ResponseBody
    public ResponseEntity getJobsByStatus(@RequestParam String status) throws SchedulerException {
        List<String> jobsList = null;
        if (!validStatusList.contains(status)) {
            return new ResponseEntity<>("Invalid status specified on request", HttpStatus.BAD_REQUEST);
        }
        if (status.equalsIgnoreCase(JobStatus.RUNNING)) {    //return running jobs from scheduler
            jobsList = scheduler.getCurrentlyExecutingJobs().stream()
                    .map(job -> job.getJobDetail().getDescription())
                    .collect(Collectors.toList());
        } else {
            //get jobs from db for status other than running
            jobsList = getJobs(status);
        }
        if(jobsList == null || jobsList.size() == 0){
            jobsList.add("Currently no jobs are in specified status");
        }
        return new ResponseEntity<>(jobsList, HttpStatus.OK);
    }

    /**
     *
     * @param status
     * @return  job names in particular status
     */
    private List<String> getJobs(String status) {
        return jobService.getAllJobs().stream()
                .filter(job -> job.getStatus().equalsIgnoreCase(status))
                .map(JobModel::getJobname)
                .collect(Collectors.toList());
    }

    /**
     * method builds trigger for requested job
     * @param jobDetail
     * @param jobModel
     * @return
     */
    private Trigger buildJobTrigger(JobDetail jobDetail, JobModel jobModel) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "job-triggers-group")
                .withDescription(jobModel.getTrigger().getName())
                .startAt(Date.from(new Date().toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobModel.getTrigger().getCron()))
                .build();
    }
}
