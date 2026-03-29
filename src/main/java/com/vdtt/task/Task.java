package com.vdtt.task;

import com.vdtt.data.DataCenter;

import java.util.ArrayList;
import java.util.List;

public class Task {
    public static TaskTemplate getTaskTemplate(int id) {
        for (TaskTemplate task : DataCenter.gI().TaskTemplate) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public int index;
    public short taskId;
    public short count;
    public TaskTemplate template;
    public List<Step> vStep;

    public Task(short taskId, byte index, short count) {
        this.taskId = taskId;
        this.index = index;
        this.count = count;
        this.template = getTaskTemplate(taskId);
        this.vStep = template.getSteps();
    }

    public boolean isComplete() {
        return index >= template.getSteps().size() - 1;
    }
}
