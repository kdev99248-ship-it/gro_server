package com.vdtt.task;

import com.vdtt.data.DataCenter;
import com.vdtt.map.MapManager;
import com.vdtt.model.Char;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskOrder {

    public static final byte TASK_NORMAL = 0;
    public static final byte TASK_ANSWER = 1;
    public static final byte TASK_TA_TL = 2;
    public static final byte TASK_MAI_RUA = 3;
    public static final byte TASK_DI_CHUYEN_DA = 4;
    public int taskId;
    public int count;
    public int maxCount;
    public String name;
    public String description;
    public int killId;
    public int mapId;
    public boolean failed;
    public Char p;

    public TaskOrder(Char p, byte type) {
        this.p = p;
        this.taskId = type;
    }

    public TaskOrder(Char p, byte taskId, int count, int maxCount, int killId, int mapId,boolean failed) {
        this.p = p;
        this.count = count;
        this.maxCount = maxCount;
        this.taskId = taskId;
        this.killId = killId;
        this.mapId = mapId;
        switch (taskId) {
            case TASK_NORMAL:
                this.name = "Nhiệm vụ thường";
                this.description = "Tiêu diệt "+ DataCenter.gI().mobTemplate[killId].name;
                break;
            case TASK_ANSWER:
                this.name = "Nhiệm vụ trả lời";
                this.description = "Học thêm kiến thức ";
                break;
            case TASK_TA_TL:
                this.name = "Nhiệm vụ Săn tinh anh thủ lĩnh";
                this.description = "Tiêu diệt "+ DataCenter.gI().mobTemplate[killId].name+" tinh anh thủ lĩnh";
                break;
            case TASK_MAI_RUA:
                this.name = "Nhiệm vụ Mai Rùa";
                this.description = "Đeo mai rùa 10kg luyện tập trong 5 phút";
                break;
            case TASK_DI_CHUYEN_DA:
                this.name = "Nhiệm vụ Di chuyển đá";
                this.description = "Di chuyển tảng đá ";
                break;
        }
        this.failed = failed;
    }

    public void setTask(int count, int maxCount, String name, String description, int killId, int mapId) {
        this.count = count;
        this.maxCount = maxCount;
        this.name = name;
        this.description = description;
        this.killId = killId;
        this.mapId = mapId;
    }

    public boolean isComplete() {
        return this.count >= this.maxCount;
    }

    public void updateTask(int count) {
        this.count += count;
        if (this.count > this.maxCount) {
            this.count = this.maxCount;
        }
        p.getService().updateTaskOrder(this);
    }
}
