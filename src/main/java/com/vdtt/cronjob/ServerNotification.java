package com.vdtt.cronjob;

import com.vdtt.server.GlobalService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class ServerNotification implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        try (Stream<String> lines = Files.lines(Paths.get("notification.txt"))) {
            lines.forEach(line -> GlobalService.getInstance().chat("Hệ thống", line, (byte) 1));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
