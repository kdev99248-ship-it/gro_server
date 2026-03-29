package com.vdtt.cronjob;

import com.vdtt.server.Server;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

public class StoreData implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Server.saveAll();
    }
}
