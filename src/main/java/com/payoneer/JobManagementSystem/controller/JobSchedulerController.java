package com.payoneer.JobManagementSystem.controller;

import com.payoneer.JobManagementSystem.constants.ClassNameConstants;
import com.payoneer.JobManagementSystem.constants.LoggerConstants;
import com.payoneer.JobManagementSystem.exception.JobScheduleException;
import com.payoneer.JobManagementSystem.job.RefreshCacheJob;
import com.payoneer.JobManagementSystem.job.SendEmailJob;
import com.payoneer.JobManagementSystem.model.JobModel;
import com.payoneer.JobManagementSystem.service.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
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

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
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
    @PostMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void scheduleEmail(@PathVariable("id") long id) throws IllegalAccessException, ClassNotFoundException, InstantiationException, SchedulerException {

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
                jobDetail = buildSendEmailJobDetail(jobModel);
            }
            else if(jobName.equalsIgnoreCase(ClassNameConstants.REFRESH_CACHE_JOB)){
                RefreshCacheJob.setJob(jobModel);
                jobDetail = RefreshCacheJob.buildRefreshCacheJobDetail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Trigger trigger = buildJobTrigger(jobDetail, jobModel);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("::starting the job ::"+jobName);
        scheduler.start();
        printSchedulerStat();
    }

    @GetMapping("/jobs")
    @ResponseBody
    public ResponseEntity getJobsByStatus(@RequestParam List<String> status) throws SchedulerException {
        status.forEach(e -> System.out.println(e));
        scheduler.getCurrentlyExecutingJobs().size();
        return null;
    }

    public void printSchedulerStat() throws SchedulerException {
        System.out.println("::::::::: in printstats ::::::::::");
        for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals("email-group"))) {

            String jobName1 = jobKey.getName();
            String jobGroup = jobKey.getGroup();

            //get job's trigger
            List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
            Date nextFireTime = triggers.get(0).getNextFireTime();

            System.out.println("[jobName] : " + jobName1 + " [groupName] : "
                    + jobGroup + " - " + nextFireTime);

        }
    }

    private JobDetail buildSendEmailJobDetail(JobModel jobModel) {
        JobDataMap jobDataMap = new JobDataMap();
        return JobBuilder.newJob(SendEmailJob.class)
                .withIdentity(UUID.randomUUID().toString(), "email-group")
                .withDescription(jobModel.getJobname())
                //.usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, JobModel jobModel) {
        System.out.println("date -" + new Date().toString());
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), "job-triggers-group")
                .withDescription(jobModel.getTrigger().getName())
                .startAt(Date.from(new Date().toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobModel.getTrigger().getCron()))
                .build();
    }
}
