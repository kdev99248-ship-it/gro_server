package com.vdtt.task;

import com.vdtt.model.Char;
import com.vdtt.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TaskFactory {
    private static final TaskFactory instance = new TaskFactory();

    public static TaskFactory getInstance() {
        return instance;
    }
    private final List<MobInfo> taskDays = new ArrayList<>();
    public void addMobInfoTaskDay(MobInfo info) {
        if (taskDays.stream().filter(m -> m.getMobID() == info.getMobID() && m.getLevel() == info.getLevel()
                && m.getMapID() == info.getMapID()).count() == 0) {
            taskDays.add(info);
        }
    }

    public int getLevellMobInfoClosestLevel(int level) {
        int levelMob = 0;
        int levelMin = -1;
        for (MobInfo info : taskDays) {
            if (levelMin == -1 || Math.abs(info.getLevel() - level) < levelMin) {
                levelMin = Math.abs(info.getLevel() - level);
                levelMob = info.getLevel();
            }
        }
        return levelMob;
    }
    public MobInfo randomMobInfoTaskDay(int level) {
        List<MobInfo> list = taskDays.stream().filter((mobInfo) -> (Math.abs(mobInfo.getLevel() - level) <= 5))
                .toList();
        int size = list.size();
        int rd = Util.nextInt(size);
        return list.get(rd);
    }
    public TaskOrder createTaskOrder(byte type, Char p) {
        TaskOrder task = new TaskOrder(p, type);
        if (type == TaskOrder.TASK_NORMAL) {
            int level = getLevellMobInfoClosestLevel(p.level());
            MobInfo info = randomMobInfoTaskDay(level);
            task.setTask(0, Util.nextInt(20, 40), "Nhiệm vụ hàng ngày",
                    "Tiêu diệt "+info.getName(), info.getMobID(), info.getMapID());
        } else if (type == TaskOrder.TASK_ANSWER) {
            task.setTask(0, Util.nextInt(5, 10), "Nhiệm vụ hàng ngày",
                    "Học thêm kiến thức ", -1, -1);
        } else if (type == TaskOrder.TASK_TA_TL) {
            int level = getLevellMobInfoClosestLevel(p.level());
            MobInfo info = randomMobInfoTaskDay(level);
            task.setTask(0, 1, "Nhiệm vụ hàng ngày",
                    "Tiêu diệt "+info.getName()+" tinh anh thủ lĩnh siêu quái", info.getMobID(), info.getMapID());
        } else if (type == TaskOrder.TASK_MAI_RUA) {
            task.setTask(0, 1, "Nhiệm vụ hàng ngày",
                    "Đeo mai rùa 10kg luyện tập trong 5 phút", -1, -1);

        } else if (type == TaskOrder.TASK_DI_CHUYEN_DA) {
            task.setTask(0, Util.nextInt(40,100), "Nhiệm vụ hàng ngày",
                    "Di chuyển tảng đá ", 221, 67);

        }
        return task;
    }

    public Task createTask(short taskID, byte taskIndex, short taskCount) {
        return new Task(taskID, taskIndex, taskCount);
    }
}
