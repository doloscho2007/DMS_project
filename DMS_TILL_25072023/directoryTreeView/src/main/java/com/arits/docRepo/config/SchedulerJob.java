/*
 File: FileProcessorSchedulerJob.java 
 Date 			    	Author 			Changes 
 May 10, 2020 	       NTT DATA 		Created
 */
package com.arits.docRepo.config;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.arits.docRepo.constants.ApplicationConstants;
import com.arits.docRepo.job.DMSProcessScheduler;




/**
 * Configuration class to load Scheduler jobs.
 * 
 * @author NTT DATA
 * @version 1.0
 * @created May 10, 2020
 */
@Configuration
public class SchedulerJob {
	
	/**
	 * Holds the value of env
	 */
	@Autowired
	private Environment env;

    /**
     * jobFileProcessor
     * 
     * Method to configure file processor execution timings.
     * 
     * @param
     * @return JobDetail
     */
    @Bean
    public JobDetail jobDMSProcessor() {
    	System.out.println("inside Job");
        return JobBuilder.newJob(DMSProcessScheduler.class).withIdentity(ApplicationConstants.DMS_PROCESSOR_JOB_NAME)
                .storeDurably().build();
    }

    /**
     * jobFileTrigger
     * 
     * Method to configure CHN order job details.
     * 
     * @param jobFileProcessor
     * @return Trigger
     */
    @Bean
    public Trigger jobDmsProcessorJobTrigger(JobDetail jobDMSProcessor) {

        return TriggerBuilder.newTrigger().forJob(jobDMSProcessor)
                .withIdentity(ApplicationConstants.DMS_PROCESSOR_TRIGGER_NAME)
                .withSchedule(CronScheduleBuilder.cronSchedule(env.getProperty(ApplicationConstants.DMS_PROCESSOR_TIMINGS)))
                .build();
    }    
    
    
   
}