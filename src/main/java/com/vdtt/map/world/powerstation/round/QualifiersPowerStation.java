package com.vdtt.map.world.powerstation.round;

import com.vdtt.item.Item;
import com.vdtt.mail.Mail;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.powerstation.PowerStation;
import com.vdtt.model.Char;
import com.vdtt.party.Group;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class QualifiersPowerStation extends RoundPowerStation {

    public QualifiersPowerStation(PowerStation powerStation) {
        super(powerStation, "Qualifiers", 2);
        setMaxTime((int) TimeUnit.MINUTES.toMillis(6) + (int) TimeUnit.SECONDS.toMillis(30));
        setPrepareTime((int) TimeUnit.MINUTES.toMillis(1));
    }

    @Override
    protected void consolationForLoserGroup(List<Group> groups) {
        for (Group group : groups) {
            for (Char player : group.getChars()) {
                player.wakeUpFromDead();
                MapManager.getInstance().joinZone(player, 86, 0, 1);
                player.serverDialog("Nhóm của bạn đã thua hẹn gặp lại bạn ở giải sau kiểm tra thư nhận quà an ủi");

                Item newItem = new Item(277, "thua giải đấu sức mạnh");
                newItem.setQuantity(5);
                newItem.setLock(true);
                Mail mail = player.createMail("Hệ thống", "Thư của hệ thống", "Quà thua giải đấu sức mạnh", 0, 50000, 0, newItem);
                player.mailManager.addMail(mail);
            }
            removeGroup(group);
        }
    }
}
