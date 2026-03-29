package com.vdtt.cronjob;

import com.vdtt.server.Server;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class AutoMaintenance implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Server.maintenance((byte) 100);
    }
}
