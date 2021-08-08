package com.payoneer.JobManagementSystem.constants;

import com.payoneer.JobManagementSystem.job.SendEmailJob;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Ashish
 */
public class ClassNameConstants implements Job {

    public final static String SEND_EMAIL_JOB = "SendEmailJob";
    public final static String REFRESH_CACHE_JOB = "RefreshCacheJob";
    public static final String CLASS_SEND_EMAIL_JOB = "com.payoneer.JobManagementSystem.job.SendEmailJob";
    public final static String CLASS_REFRESH_CACHE_JOB = "com.payoneer.JobManagementSystem.job.RefreshCacheJob";


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
