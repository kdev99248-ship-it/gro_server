package com.vdtt.map.world.planetblackstar;

import com.vdtt.effect.Effect;
import com.vdtt.item.Item;
import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.MapTemplate;
import com.vdtt.map.world.planetblackstar.dragon.*;
import com.vdtt.map.zones.Zone;
import com.vdtt.mob.*;
import com.vdtt.model.Char;
import com.vdtt.server.GlobalService;
import com.vdtt.util.Log;
import com.vdtt.util.Util;

import java.util.List;

public class ZPlanetBlackStar extends Zone {
    private long lastTimeNotify;

    public ZPlanetBlackStar(int id, MapTemplate tilemap, Map map) {
        super(id, tilemap, map);
    }

    @Override
    public void join(Char p, int typeTau) {
        try {
            if (p.clan == null) {
                p.getService().serverDialog("Chưa vào vũ trụ");
                return;
            }
            Zone preZone = p.zone;
            p.mapId = this.template.id;
            if (preZone != null) {
                preZone.out(p);
                if (preZone.map.id != this.map.id) {
                    int[] xy;
                    if (typeTau == -1) {
                        xy = getArrayXYNext(preZone.map.id);
                    } else {
                        xy = getArrayXYRND();
                    }
                    p.updateXY2(xy);
                }
            }
            p.zone = this;
            if (p.pet != null) {
                p.pet.zone = this;
            }
            p.setTypePk((byte) 0);
            for (Char c : getChars()) {
                if (p.clan.equals(c.clan) && c.itemMapBlackDragonBall != null) {
                    p.setTypePk((byte) c.typePK);
                    break;
                }
            }
            // thêm player vào khu cho người chơi khác
            p.getService().sendZone(this, typeTau);
            getService().playerAdd(p);
            getService().sendInfoVuTru(p);
            p.getEm().displayAllEffect(this.getService(), p);
            addChar(p);
            loadMap(p);// load người trong map
            getService().changePk(p);
        } catch (Exception e) {
            Log.error(e);
        }
    }

    @Override
    public Boss createBoss(short id, int hp, short level, short x, short y, int kimax) {
        int uniqueId = monsters.size();
        switch (id) {
            case 0 -> {
                return new Rong1Sao(uniqueId, 114, hp, level, x, y, kimax);
            }
            case 1 -> {
                return new Rong2Sao(uniqueId, 113, hp, level, x, y, kimax);
            }
            case 2 -> {
                return new Rong3Sao(uniqueId, 112, hp, level, x, y, kimax);
            }
            case 3 -> {
                return new Rong4Sao(uniqueId, 111, hp, level, x, y, kimax);
            }
            case 4 -> {
                return new Rong5Sao(uniqueId, 110, hp, level, x, y, kimax);
            }
            case 5 -> {
                return new Rong6Sao(uniqueId, 109, hp, level, x, y, kimax);
            }
            case 6 -> {
                return new Rong7Sao(uniqueId, 108, hp, level, x, y, kimax);
            }
            default -> throw new IllegalStateException("Unexpected value: " + id);
        }
    }

    @Override
    public void updateChar() {
        try {
            long now = System.currentTimeMillis();
            boolean isUpdateEverySecond = ((now - this.lastUpdateEverySecond) >= 1000);
            if (isUpdateEverySecond) {
                this.lastUpdateEverySecond = now;
            }
            boolean isUpdateEveryHalfSecond = ((now - this.lastUpdateEveryHalfSecond) >= 500);
            if (isUpdateEveryHalfSecond) {
                this.lastUpdateEveryHalfSecond = now;
            }
            boolean isUpdateEveryFiveSecond = ((now - this.lastUpdateEveryFiveSecond) >= 5000);
            if (isUpdateEveryFiveSecond) {
                this.lastUpdateEveryFiveSecond = now;
            }
            boolean isUpdateEveryMinute = ((now - this.lastUpdateEveryMinute) >= 60000);
            if (isUpdateEveryMinute) {
                this.lastUpdateEveryMinute = now;
            }
            if (isUpdateEveryHalfSecond || isUpdateEverySecond || isUpdateEveryFiveSecond || isUpdateEveryMinute) {
                List<Char> mChars = getChars();
                for (Char _char : mChars) {
                    try {
                        if (isUpdateEveryHalfSecond) {
                            _char.updateEveryHalfSecond();
                        }
                        if (isUpdateEverySecond) {
                            _char.updateEverySecond();
                        }
                        if (isUpdateEveryFiveSecond) {
                            _char.updateEveryFiveSecond();
                        }
                        if (isUpdateEveryMinute) {
                            _char.updateEveryMinute();
                        }
                        if (_char.itemMapBlackDragonBall != null) {
                            if (System.currentTimeMillis() - _char.lastTimePickBall > PlanetBlackStar.TIME_WIN) {
                                win(_char);
                                Effect eff = _char.getEm().findByID(132);
                                if (eff != null) {
                                    _char.getEm().removeEffect(eff);
                                    getService().playerRemoveEffect(_char, eff);
                                }
                                return;
                            } else {
                                if (System.currentTimeMillis() - lastTimeNotify > 10000) {
                                    int timeLeft = (int) ((_char.lastTimePickBall - (System.currentTimeMillis() - PlanetBlackStar.TIME_WIN)) / 1000);
                                    _char.getService().serverMessage("Cố gắng giữ " + _char.itemMapBlackDragonBall.getItem().getTemplate().name + " trong " + (timeLeft < 60 ? timeLeft + " giây" : timeLeft / 60 + " phút") + " nữa, bạn sẽ chiến thắng!");
                                    lastTimeNotify = System.currentTimeMillis();
                                }
                            }
                        }
                    } catch (Exception e) {
                        Log.error(e);
                    }
                }
            }
            if (!PlanetBlackStarManager.isStart()) {
                for (Char charz : this.getChars()) {
                    charz.setXY(1430, 430);
                    MapManager.getInstance().joinZone(charz, 86, 0, 1);
                    charz.getService().serverDialog("Ngọc rồng đen kết thúc");
                }
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void dropBall(Char pl) {
        if (pl.itemMapBlackDragonBall != null) {
            Effect eff = pl.getEm().findByID(132);
            if (eff != null) {
                pl.getEm().removeEffect(eff);
                getService().playerRemoveEffect(pl, eff);
            }
            for (Char player : pl.clan.getOnlineMembers()) {
                if (player.zone instanceof ZPlanetBlackStar) {
                    player.setTypePk((byte) 0);
                }
            }
            AbsDragon absDragon = (AbsDragon) this.createBoss((short) (pl.itemMapBlackDragonBall.star - 1), 30000000, (short) 10, (short) 542, (short) 505, 1000);
            absDragon.zone = this;
            this.addMob(absDragon);
            pl.itemMapBlackDragonBall = null;
        }
    }

    public void win(Char pl) {
        if (pl.itemMapBlackDragonBall != null) {
            Effect eff = pl.getEm().findByID(132);
            if (eff != null) {
                pl.getEm().removeEffect(eff);
                getService().playerRemoveEffect(pl, eff);
            }
            GlobalService.getInstance().chat("Hệ thống", "Vũ trụ " + pl.clan.name + " đã chiếm được ngọc rồng đen " + pl.itemMapBlackDragonBall.star + " sao , thật ngưỡng mộ", (byte) 0);
            Item item = pl.itemMapBlackDragonBall.getItem();
            item.setQuantity(1);
            item.isLock = true;
            pl.clan.addItem(item);
            pl.itemMapBlackDragonBall = null;
            for (Char charz : pl.zone.getChars()) { // kích tat ca ngoi choi trong khu
                charz.setXY(1430, 430);
                MapManager.getInstance().joinZone(charz, 86, 0, 1);
            }
        }
    }

    public void createNewItemMapStar(AbsDragon absDragon, Char killer, int star) {
        int itemId = switch (star) {
            case 1 -> 917;
            case 2 -> 916;
            case 3 -> 915;
            case 4 -> 914;
            case 5 -> 913;
            case 6 -> 912;
            case 7 -> 911;
            default -> -1;
        };
        if (itemId == -1) {
            return;
        }
        Item item = new Item(itemId, "BallBlack " + star + " star");
        item.setQuantity(1);
        item.expire = -1;
        item.isLock = false;

        ItemMapBlackDragonBall itemMap = new ItemMapBlackDragonBall((short) itemId);
        itemMap.setX((short) (absDragon.x + Util.nextInt(-30, 50)));
        itemMap.setY((short) absDragon.y);
        itemMap.setOwnerID(killer.id);
        itemMap.setItem(item);
        itemMap.star = star;

        absDragon.zone.addItemMap(itemMap);
        absDragon.zone.getService().addItemMap(itemMap, killer);
    }
}
