package com.vdtt.server;

import com.vdtt.data.StringMenu;
import com.vdtt.leaderboard.LeaderboardRace;
import com.vdtt.leaderboard.LeaderboardRaceManager;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Log;
import com.vdtt.util.TimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class RuntimeServer implements Runnable {

    private static final RuntimeServer instance = new RuntimeServer();

    public static RuntimeServer gI() {
        return instance;
    }

    public long timeStart = 0;
    private HashSet<Integer> playerIDs = new HashSet<>();

    public void reset() {
        playerIDs.clear();
    }

    public boolean callDragon = false;
    private long timeWait = 0;
    public synchronized void uocRongALL(Char pl, byte type) {
        try {
            if(!TimeUtils.canDoWithTime(timeWait, 5)) {
                pl.getService().serverMessage("Vui lòng đợi 5s.");
                return;
            }
            timeWait = System.currentTimeMillis();
            GlobalService.getInstance().chat("Hệ thống", "Bầu trời vừa tối lại, c#item" + pl.name + "c#white đã triệu hồi rồng thần", (byte) 1);
            timeStart = System.currentTimeMillis() + 180000;
            callDragon = true;
            RuntimeServer.gI().uocRong(pl.id, (short) 440);
            if (type == 0) {
                // xóa ngọc rồng từ lúc ước
                int index = pl.getIndexItemByIdInBag(1045, true);
                int index2 = pl.getIndexItemByIdInBag(1046, true);
                if (index == -1 || index2 == -1) {
                    return;
                }
                pl.removeItem(index, 1, true);
                pl.removeItem(index2, 1, true);

                pl.getService().effectDragon((short) 478);
                pl.getService().menuUocRong(StringMenu.Cerealian, (byte) 4);
            } else if (type == 1) {
                // xóa ngọc rồng từ lúc ước
                List<Integer> indexItem = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    int index = pl.getIndexItemByIdInBag(839 + i, true);
                    if (index < 0) {
                        continue;
                    }
                    indexItem.add(index);
                }
                if (indexItem.size() < 7) {
                    pl.serverDialog("Không đủ 7 viên ngọc rồng");
                    RuntimeServer.gI().resetEffectUoc();
                    return;
                }
                for (int index : indexItem) {
                    pl.removeItem(index, 1, true);
                }
                pl.getService().effectDragon((short) 441);

//                450 rồng namec
//                479 rồng sc
                pl.getService().menuUocRong(StringMenu.Eath, (byte) 4);
            } else if (type == 2) {
                pl.getService().effectDragon((short) 449); //nrd
                pl.getService().menuUocRong(StringMenu.Black_dragon, (byte) 4);
            } else if (type == 3) {
                List<Integer> indexItem = new ArrayList<>();
                for (int i = 0; i < 7; i++) {
                    int index = pl.getIndexItemByIdInBag(901 + i, true);
                    if (index < 0) {
                        continue;
                    }
                    indexItem.add(index);
                }
                if (indexItem.size() < 7) {
                    pl.serverDialog("Không đủ 7 viên ngọc rồng namec");
                    RuntimeServer.gI().resetEffectUoc();
                    return;
                }
                for (int index : indexItem) {
                    pl.removeItem(index, 1, true);
                }
                pl.getService().effectDragon((short) 450);
                pl.getService().menuUocRong(StringMenu.Namec, (byte) 4);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uocRong(int idPlayer, short id) {
        try {
            Message m = new Message((byte) 3);
            m.writer().writeInt(idPlayer);
            m.writer().writeShort(id);
            GlobalService.getInstance().sendMessage(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetEffectUoc() {
        try {
            timeStart = 0;
            callDragon = false;
            Message m = new Message((byte) 3);
            m.writer().writeInt(-1);
            m.writer().writeShort(440);
            GlobalService.getInstance().sendMessage(m);
        } catch (Exception e) {

        }
    }

    @Override
    public void run() {
        while (Server.start) {
            try {
                if (timeStart > 0 && timeStart < System.currentTimeMillis()) {
                    resetEffectUoc();
                }
                for (LeaderboardRace lb : LeaderboardRaceManager.getInstance().getLeaderboardRaces()) {
                    if(lb.getId() < 7 && lb.isEnded() && !lb.isPrizeGiven()) {
                        switch (lb.getId()) {
                            case 0:
                                Ranked.initTopCaoThu();
                                break;
                            case 1:
                                Ranked.initTopPhamChat();
                                break;
                            case 2:
                                Ranked.initTopNap();
                                break;
                            case 3:
                                Ranked.initTopVuTru();
                                break;
                              case 4:
                                Ranked.initTopNapThang();
                                break;   
                                
                        }
                        lb.givePrize(Ranked.RANKED[lb.getId()]);
                    }
                }
                
                Thread.sleep(500L);
            } catch (Exception e) {
                Log.error("RuntimeServer", e);
            }
        }
    }
}
