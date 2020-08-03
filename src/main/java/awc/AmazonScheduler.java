package awc;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import awc.quartz.job.AmazonJob;

public class AmazonScheduler
{
    public static void main (String[] args)
            throws Exception
    {
        // schedule a factory
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        // create scheduler
        Scheduler sched = schedFact.getScheduler();
        sched.start();

        // set up our awc.quartz.job.AmazonJob class
        JobDetail job = newJob(AmazonJob.class)
                .withIdentity("myJob", "group1")
                .build();

        // Trigger the job to run now, and then every 40 seconds
        Trigger trigger = newTrigger()
                .withIdentity("myTrigger", "group1")
                .startNow()
                .withSchedule(simpleSchedule()
                                      .withIntervalInSeconds(5)
                                      .repeatForever())
                .build();
        //
        //        JobDetail job2 = newJob(awc.quartz.job.DumbJob.class);
        //                .withIdentity("myJob", "group1") // name "myJob", group "group1"
        //                .usingJobData("jobSays", "Hello World!")
        //                .usingJobData("myFloatValue", 3.141f)
        //                .build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);
    }

}