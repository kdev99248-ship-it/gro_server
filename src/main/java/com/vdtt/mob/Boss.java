package com.vdtt.mob;

import com.vdtt.server.GlobalService;
import lombok.Getter;
import lombok.Setter;

public class Boss extends Mob {

    @Getter
    private String notifyText;
    private long lastNotifyTime;

    public Boss(int idEntity, int id, int hp, int level, int x, int y, int kiMax) {
        super(idEntity, id, hp, level, x, y, kiMax);
    }

    @Override
    protected void setHP() {
        this.hp = this.hpFull;
    }

    @Override
    public void update() {
        super.update();
//        if (!isDead && notifyText != null) {
//            long now = System.currentTimeMillis();
//            if (now - lastNotifyTime >= 600_000L) {
//                lastNotifyTime = now;
//                GlobalService.getInstance().chat("Hệ thống", notifyText, (byte) 1);
//            }
//        }
    }

    public void setNotifyText(String notifyText) {
        this.notifyText = notifyText;
        GlobalService.getInstance().chat("Hệ thống", notifyText, (byte) 1);
    }
}
