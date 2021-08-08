/*
package com.payoneer.JobManagementSystem.configuration;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

*/
/**
 *
 *//*

@Configuration

public class SchedulerConfig {

    private static Scheduler scheduler;

    public SchedulerConfig() {
    }

    public static void setInstance(Scheduler aInstance) {
        scheduler = aInstance;
    }

    public static Scheduler getInstance() throws SchedulerException {
        if (scheduler == null) {
            synchronized (SchedulerConfig.class) {
                if (scheduler == null) {
                    SchedulerFactory schedulerFactory = new StdSchedulerFactory();
                    scheduler = schedulerFactory.getScheduler();
                }
            }
        }
        return scheduler;
    }
}
*/
