package com.payoneer.JobManagementSystem.constants;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Ashish Dandekar
 */
public class ClassNameConstants implements Job {

    public final static String SEND_EMAIL_JOB = "SendEmailJob";
    public final static String REFRESH_CACHE_JOB = "RefreshCacheJob";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
