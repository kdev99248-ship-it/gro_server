package com.vdtt.cronjob;

import com.vdtt.util.Log;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class CronJobSetup {

    private Scheduler scheduler;

    public void setup() {
        try {
            scheduler = StdSchedulerFactory.getDefaultScheduler();
            addJob(StoreData.class, "0 */5 * ? * *");
            addJob(ServerNotification.class, "0 */10 * ? * *");
            addJob(AutoMaintenance.class, "0 0 10 ? * * *");
        } catch (SchedulerException e) {
            Log.error("Setup cronjob failure!", e);
        }
    }

    public void start() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            Log.error("Start cronjob failure!", e);
        }
    }

    public void shutdown() {
        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addJob(Class<? extends Job> jobClass, String cronExpression) throws SchedulerException {
        if (scheduler == null) {
            throw new SchedulerException("Scheduler not started. Call start() method first.");
        }

        JobDetail job = JobBuilder.newJob(jobClass)
                .withIdentity(jobClass.getSimpleName(), "group1")
                .build();

        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobClass.getSimpleName() + "Trigger", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
                .build();

        scheduler.scheduleJob(job, trigger);
    }

    private static class SingletonHolder {
        private static final CronJobSetup INSTANCE = new CronJobSetup();
    }

    public static CronJobSetup getInstance() {
        return CronJobSetup.SingletonHolder.INSTANCE;
    }
}
