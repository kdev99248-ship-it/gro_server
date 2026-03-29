package com.vdtt.server;

import com.vdtt.events.AbsEvent;
import com.vdtt.events.EventHandler;
import com.vdtt.events.points.EventPoint;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.map.world.martialarts.MartialArtsConference;
import com.vdtt.map.world.martialarts.ZMartialArts;
import com.vdtt.map.world.powerstation.PowerStation;
import com.vdtt.map.world.powerstation.ZPowerTournament;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

public class ShowRanked {
    private static final ShowRanked instance = new ShowRanked();

    public static ShowRanked gI() {
        return instance;
    }
    public void show(Message ms, Char pl) {
        try {
            byte type = ms.reader().readByte();
            byte option = ms.reader().readByte();
            switch (type) {
                case 0:
                    switch (option) {
                        case 0:
                            if(pl.zone instanceof ZMartialArts) {
                                MartialArtsConference.current.showTopMatch(pl);
                            } else if (pl.zone instanceof ZPowerTournament) {
                                PowerStation.current.showTopMatch(pl);
                            } else {
                                pl.getService().showTopCaoThu(Ranked.RANKED[0], true);
                            }
                            break;
                        case 1:
                            pl.getService().showTopCaoThu(getTopByClass((byte) 1, Ranked.RANKED[0]), true);
                            break;
                        case 2:
                            pl.getService().showTopCaoThu(getTopByClass((byte) 2, Ranked.RANKED[0]), true);
                            break;
                        case 3:
                            pl.getService().showTopCaoThu(getTopByClass((byte) 3, Ranked.RANKED[0]), true);
                            break;
                        case 4:
                            pl.getService().showTopCaoThu(getTopByClass((byte) 4, Ranked.RANKED[0]), true);
                            break;
                        case 5:
                            pl.getService().showTopCaoThu(getTopByClass((byte) 5, Ranked.RANKED[0]), true);
                            break;
                    }
                    break;
                case 4:// top vu tru
                    pl.getService().showTopVuTru(Ranked.RANKED[3], true);
                    break;
                case 3:// top nap the
                    pl.getService().showTopNap(Ranked.RANKED[2], true);
                    break;
                case 2:// top pham chat
                    pl.getService().showTopPhamChat(Ranked.RANKED[1]);
                    break;
                case 21:
                    pl.getService().showTopSoiNoi(Ranked.topSoiNoi);
                    break;
                  case 5:// top nap the
                    pl.getService().showTopNap(Ranked.RANKED[2], true);
                    break;    
                    
                default:
                    pl.getService().showTopCaoThu(new Vector<>(), false);
                    break;
            }
            if (type == 8) {
                List<CaoThu> caoThus = Ranked.topNapThang;
                try {
                    Message m = new Message((byte) -22);
                    m.writer().writeBoolean(true);
                    m.writer().writeByte(caoThus.size());
                    for (int i = 0; i < caoThus.size(); i++) {
                        CaoThu caoThu = caoThus.get(i);
                        m.writer().writeByte(i);
                        m.writeUTF(caoThu.name);
                        m.writer().writeLong(0);
                        m.writer().writeLong(caoThu.num);
                        m.writer().writeByte(caoThu.classId);
                        m.writeUTF(Objects.toString(caoThu.clanName, "Chưa vào vũ trụ"));
                    }
                    pl.getService().sendMessage(m);
                    m.cleanup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
//            if (type == 10) {
//                List<CaoThu> caoThus = Ranked.topLamBanh;
//                try {
//                    Message m = new Message((byte) -22);
//                    m.writer().writeBoolean(true);
//                    m.writer().writeByte(caoThus.size());
//                    for (int i = 0; i < caoThus.size(); i++) {
//                        CaoThu caoThu = caoThus.get(i);
//                        m.writer().writeByte(i);
//                        m.writeUTF(caoThu.name);
//                        m.writer().writeLong(0);
//                        m.writer().writeLong(caoThu.num);
//                        m.writer().writeByte(caoThu.classId);
//                        m.writeUTF(Objects.toString(caoThu.clanName, "Chưa vào vũ trụ"));
//                    }
//                    pl.getService().sendMessage(m);
//                    m.cleanup();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return;
//            }
//            if (type == 11) {
//                List<CaoThu> caoThus = Ranked.topThaLongDen;
//                try {
//                    Message m = new Message((byte) -22);
//                    m.writer().writeBoolean(true);
//                    m.writer().writeByte(caoThus.size());
//                    for (int i = 0; i < caoThus.size(); i++) {
//                        CaoThu caoThu = caoThus.get(i);
//                        m.writer().writeByte(i);
//                        m.writeUTF(caoThu.name);
//                        m.writer().writeLong(0);
//                        m.writer().writeLong(caoThu.num);
//                        m.writer().writeByte(caoThu.classId);
//                        m.writeUTF(Objects.toString(caoThu.clanName, "Chưa vào vũ trụ"));
//                    }
//                    pl.getService().sendMessage(m);
//                    m.cleanup();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return;
//            }
            if (EventHandler.isEvent()) {
                AbsEvent event = EventHandler.getEvent();
                List<KeyPoint> keys = event.getKeys().stream().filter(KeyPoint::isRace).toList();
                int startType = 10;
                int endType = 10 + keys.size() - 1;
                if (type >= startType && type <= endType) {
                    event.viewRace(pl, keys.get(type - startType));
                }
            }
            Log.debug("ShowRanked: " + type + " - " + option);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vector<CaoThu> getTopByClass(byte classId, Vector<CaoThu> list) {
        Vector<CaoThu> result = new Vector<>();
        for (int i = 0; i < list.size(); i++) {
            CaoThu pl = list.elementAt(i);
            if (pl.classId == classId) {
                result.addElement(pl);
            }
        }
        return result;

    }

}
