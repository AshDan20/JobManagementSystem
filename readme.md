**APPLICATION TO TAKE CARE OF CREATING AND SCHEDULING JOBS**

**Scope Covered**
1. Jobs are maintained in H2 DB for this assignment
2. Jobs are assigned with Trigger ID which has a cron expression specified
3. Jobs or Trigger can be added or updated into DB or can be deleted from DB
4. Job can bring into schedule by a scheduler API as mentioned later in this document
5. API has been developed to get Job names as per the given status (mentioned later in this doc)
6. Job's current status is stored in DB, however running jobs can be fetched from scheduler
7. Test cases have been provided [mvn clean, mvn test should work fine for project]
8. Job can be scheduled as per the requirement by updating the trigger against it and executing scheduler API
9. Test data is being loaded to DB when app runs first time. It is provided in 'data.sql'
10. SendEmailJob and RefreshCacheJob have been configured (to do task). Only scheduler request for these 2 jobs would work. [Other jobs might not be scheduled]

**Time Spent to build the solution**
12-14 hrs


**Future Scope**
1. Maintain history of the job runs
2. to provide an API to pause and resume job
3. to provide an API to kill the job
4. configure other jobs to do task 


**JOB APIs**
1. get all the jobs from DB- [get request]
    e.g http://localhost:8080/job 

2. Delete Job by Job ID - [DELETE request]
    e.g. http://localhost:8080/deleteJob/{id}

3. Update a Job - [PUT request]
    e.g.  http://localhost:8080/job


**JOB Scheduler APIs**
1. Schedule a particular job... This would schedule a job and then it would be triggered as per CRON expression defined in DB
    1.1 http://localhost:8080/scheduler/1 - [schedules SendEmailJob Job - runs every 2 minutes] 
    
    1.2 http://localhost:8080/scheduler/7 - [schedules RefreshCacheJob Job - runs every 5 minutes]

2. get jobs status-wise -    [get request]
    http://localhost:8080/scheduler/jobs?status=RUNNING  - [checked from scheduler]
    http://localhost:8080/scheduler/jobs?status=QUEUED   - get from DB
    http://localhost:8080/scheduler/jobs?status=FAILED   - get from DB
    http://localhost:8080/scheduler/jobs?status=SUCCESS  - get from DB 


**TRIGGER APIs**
1. Get all triggers - [GET requesst]
   e.g http://localhost:8080/trigger
   
2. Create a trigger - [POST request]
    e.g http://localhost:8080/trigger
    
3. Get Trigger By ID - [GET request]
    e.g. http://localhost:8080/trigger/{id}
    
4. Delete Trigger by ID - [DELETE request]
    e.g. http://localhost:8080/trigger/{id}
    
    

**HOW TO TRIGGER JOB**

below jobs are configured in this example. Initial status of the job would be QUEUED


1 [JOB_ID]      'SendEmailJob'[JOBNAME]       3 [TRIGGER_ID]              'Email-Job-Group' [JOB_GROUP]      'QUEUED' [STATUS]

7 [JOB_ID]     'RefreshCacheJob'[JOBNAME]     4 [TRIGGER_ID]              'Cache-Group' [JOB_GROUP]          'QUEUED' [STATUS]

Below is an **POST** API to schedule the job. After hitting this API job would be scheduled and would run as per the cron expression configured in BE


1. http://localhost:8080/scheduler/1 - [schedules SendEmailJob Job]

2. http://localhost:8080/scheduler/7 - [schedules RefreshCacheJob Job]

- Once the job runs successfully, the status of the job is changed to **SUCCESS** in DB
- in case of any failure while running the job, status is updated to **FAILED** in DB



------------------------------------------------[LOGS FOR REFERENCE] ----------------------------------------------------
03:10:50.174  INFO 20268 --- [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2021-08-12 03:10:50.174  INFO 20268 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2021-08-12 03:10:50.175  INFO 20268 --- [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 1 ms
2021-08-12 03:10:50.280  INFO 20268 --- [nio-8080-exec-1] c.p.J.controller.JobSchedulerController  : SCHEDULED - [jobName]:SendEmailJob [groupName]:Email-Job-Group [NextFireTime]:Thu Aug 12 03:12:00 IST 2021
2021-08-12 03:10:50.280  INFO 20268 --- [nio-8080-exec-1] org.quartz.core.QuartzScheduler          : Scheduler quartzScheduler_$_NON_CLUSTERED started.
2021-08-12 03:11:00.345  INFO 20268 --- [nio-8080-exec-2] c.p.J.controller.JobSchedulerController  : SCHEDULED - [jobName]:RefreshCacheJob [groupName]:Cache-Group [NextFireTime]:Thu Aug 12 03:15:00 IST 2021
2021-08-12 03:11:00.345  INFO 20268 --- [nio-8080-exec-2] org.quartz.core.QuartzScheduler          : Scheduler quartzScheduler_$_NON_CLUSTERED started.
2021-08-12 03:12:00.011  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:12:00.104  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:13:00.106  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:14:00.006  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:14:00.010  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:15:00.004  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:15:00.009  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:15:00.015  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:16:00.007  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:16:00.011  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:17:00.015  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:18:00.002  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:18:00.010  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:18:00.027  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:19:00.014  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:20:00.004  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:20:00.006  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:20:00.008  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:20:00.010  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:21:00.018  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:22:00.012  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:22:00.014  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:23:00.015  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:23:00.020  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:24:00.006  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:24:00.010  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:25:00.001  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:25:00.005  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:25:00.016  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:26:00.002  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:26:00.007  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:27:00.012  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:28:00.005  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:28:00.008  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:28:00.018  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:29:00.013  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:30:00.009  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:30:00.011  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:30:00.012  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:30:00.014  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:31:00.017  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:32:00.008  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:32:00.012  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:33:00.016  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:33:00.020  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:34:00.001  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:34:00.007  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:35:00.002  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:35:00.005  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:35:00.014  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:36:00.002  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:36:00.005  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:37:00.010  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:38:00.002  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:38:00.005  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:38:00.010  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:39:00.018  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:40:00.001  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:40:00.007  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:40:00.007  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:40:00.009  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:41:00.015  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:42:00.001  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:42:00.004  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:43:00.008  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:43:00.011  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:44:00.002  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:44:00.006  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:45:00.000  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:45:00.003  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:45:00.011  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:46:00.002  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:46:00.005  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:47:00.011  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:48:00.004  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:48:00.009  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:48:00.025  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:49:00.017  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:50:00.003  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:50:00.007  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:50:00.008  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:50:00.011  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:51:00.016  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:52:00.014  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:52:00.019  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:53:00.018  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:53:00.025  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:54:00.001  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:54:00.004  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:55:00.002  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 03:55:00.006  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 03:55:00.011  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:56:00.005  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:56:00.012  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:57:00.018  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 03:58:00.002  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 03:58:00.007  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 03:58:00.012  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 03:59:00.017  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:00:00.000  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:00:00.002  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:00:00.003  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:00:00.007  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:01:00.012  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:02:00.001  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:02:00.005  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:03:00.008  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:03:00.012  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:04:00.003  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:04:00.007  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:05:00.001  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:05:00.006  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:05:00.014  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:06:00.006  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:06:00.009  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:07:00.012  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:08:00.004  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:08:00.007  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:08:00.012  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:09:00.011  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:10:00.002  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:10:00.003  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:10:00.006  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:10:00.006  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:11:00.012  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:12:00.000  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:12:00.003  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:13:00.013  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:13:00.016  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:14:00.000  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:14:00.004  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:15:00.009  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:15:00.012  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:15:00.018  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:16:00.006  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:16:00.009  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:17:00.013  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:18:00.009  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:18:00.012  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:18:00.019  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:19:00.016  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:20:00.002  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:20:00.005  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:20:00.006  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:20:00.009  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:21:00.013  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:22:00.001  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:22:00.004  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:23:00.007  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:23:00.013  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:24:00.002  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:24:00.004  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:25:00.000  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:25:00.002  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:25:00.013  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:26:00.000  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:26:00.005  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:27:00.009  INFO 20268 --- [eduler_Worker-1] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:28:00.001  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:28:00.006  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:28:00.010  INFO 20268 --- [eduler_Worker-3] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:29:00.013  INFO 20268 --- [eduler_Worker-5] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:30:00.001  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:30:00.002  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:30:00.010  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:30:00.012  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:31:00.015  INFO 20268 --- [eduler_Worker-4] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:32:00.001  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:32:00.004  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:33:00.009  INFO 20268 --- [eduler_Worker-6] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:33:00.015  INFO 20268 --- [eduler_Worker-7] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:34:00.000  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:34:00.004  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:35:00.002  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : TRIGGERED
2021-08-12 04:35:00.006  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : RUNNING....
2021-08-12 04:35:00.010  INFO 20268 --- [eduler_Worker-8] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:36:00.002  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:36:00.004  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:37:00.006  INFO 20268 --- [eduler_Worker-9] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED
2021-08-12 04:38:00.001  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : TRIGGERED
2021-08-12 04:38:00.006  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : RUNNING....
2021-08-12 04:38:00.011  INFO 20268 --- [duler_Worker-10] c.p.J.controller.JobSchedulerController  : [jobName] : RefreshCacheJob : COMPLETED
2021-08-12 04:39:00.012  INFO 20268 --- [eduler_Worker-2] c.p.J.controller.JobSchedulerController  : [jobName] : SendEmailJob : COMPLETED






