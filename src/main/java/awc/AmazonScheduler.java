package awc;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;

import awc.quartz.job.AmazonJob;

public class AmazonScheduler
{
    public static LinkedList<String> list = new LinkedList<String>();

    public static PrintWriter writer;

    public static HashSet<String> visited;

    public static void main (String[] args)
            throws Exception
    {
        // schedule a factory
        SchedulerFactory schedFact = new org.quartz.impl.StdSchedulerFactory();

        // create scheduler
        Scheduler sched = schedFact.getScheduler();

        sched.start();

        int seen = 0;

        writer = new PrintWriter(new FileWriter(new File("amazonReviewSentiments.txt")));

        writer.println(
                "RECORD_ID,RECORD_DATETIME,RECORD_URL,RECORD_TITLE,RECORD_TEXT,DOMAIN_ROOT_URL,CITY_NAME,STATE_CODE,COUNTRY_CODE,GPS_COORDINATES,AUTHOR_ID,AUTHOR_HANDLE,AUTHOR_NAME,AUTHOR_GENDER,AUTHOR_DESCRIPTION,_AUTHOR_PROFILE_URL,AUTHOR_AVATAR_URL,AUTHOR_FOLLOWERS,AUTHOR_VERIFIED_STATUS,META_TAGS,META_TAGS2,NET_PROMOTER_SCORE,OVERALL_STAR_RATING,OVERALL_SURVEY_SCORE,SOURCE_TYPE");
        writer.flush();

        // set up our awc.quartz.job.AmazonJob class
        JobDetail job = newJob(AmazonJob.class)
                .withIdentity("myJob", "group1")
                .usingJobData("url", list.get(0))
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