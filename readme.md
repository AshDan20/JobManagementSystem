**APPLICATION TO TAKE CARE OF CREATING AND SCHEDULING JOBS (FROM DATABASE)**


**JOB APIs**
1. get all the jobs - http://localhost:8080/job
2. get jobs statuswise - 
    http://localhost:8080/scheduler/jobs?status=RUNNING
    http://localhost:8080/scheduler/jobs?status=QUEUED
    http://localhost:8080/scheduler/jobs?status=FAILED
    http://localhost:8080/scheduler/jobs?status=SUCCESS


**HOW TO TRIGGER JOB JOB**

below jobs are configured in this design. Initial status of the job would be QUEUED
JobId   Jobname,         Trigger_id,         Jobgroup,           Status
------|------------------|--------------|-----------------------|-----------
1       'SendEmailJob'       3              'Email-Job-Group'     'QUEUED'

7      'RefreshCacheJob'     4              'Cache-Group'         'QUEUED'

Below is an API to schedule the job. After hitting this API job would be scheduled and would run as per the cron expression in DB

http://localhost:8080/scheduler/1 - schedules SendEmailJob Job

http://localhost:8080/scheduler/7 - schedules RefreshCacheJob Job



