package com.vdtt.handler.item;

import com.vdtt.db.DbManager;
import com.vdtt.events.SummerParty;
import com.vdtt.events.TrungThu2024;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.util.Util;

import java.util.concurrent.TimeUnit;

public class UseCakeTrungThu extends UseItem {

    public UseCakeTrungThu() {
        super(TrungThu2024.class, "Vật phẩm chỉ có thể sử dụng trong sự kiện Trung thu đoàn viên");
    }

    @Override
    protected void use(Char player, Item item) {
        if (player.getSlotNull() == 0) {
            player.warningBagFull();
            return;
        }
        int itemId = -1;
        if (item.id == 1137) {
            itemId = RandomItem.ITEM_EVENT_NORMAL.next();
        } else if (item.id == 1138) {
            itemId = RandomItem.ITEM_EVENT_VIP.next();
        } else if (item.id > 1138 && item.id < 1143) {
            int exp = Util.nextInt(100000, 150000);
            int ki = Util.nextInt(100000, 150000);
            if (player.exp <= 230000000000L) {
                if (!player.lockEXP) {
                    player.exp += exp;
                    player.KI += ki;
                    player.getService().showKI();
                }
            }
            player.removeItem(item.index, 1, true);
            return;
        } else if (item.template.id == 1146) {
            itemId = RandomItem.ITEM_EVENT_VIP.next();
        }
        Item newItem = new Item(itemId, "sk");
        newItem.setQuantity(1);
//        newItem.expire = -1;
        if (item.isLock) {
            newItem.isLock = true;
        } else {
            newItem.isLock = false;
        }
        if (newItem.id >= 1114 && newItem.id <= 1129) {
            newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
            newItem.addOptionRandom(433, 1, 80);
        } else if (newItem.id >= 918 && newItem.id <= 920) {
            newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
            newItem.strOption = "0,3000;1,3000;2,350;5,350;258,350;151,350;167,350;297,50";
        } else if (newItem.id >= 896 && newItem.id <= 898) {
            newItem.setExpire(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
            newItem.strOption = "0,2000;1,2000;2,200;5,200;258,200;151,200;167,200;297,20";
        } else if (newItem.id == 1050 || newItem.id == 1051) {
            newItem.strOption = "0,1000;2,150;5,10";
        }
        int newId = newItem.id;
        if (newId == 989) {
            newItem.addOptionRandom(0, 2000, 3500);
            newItem.addOptionRandom(2, 150, 315);
            newItem.addOptionRandom(5, 150, 315);
            newItem.addOptionRandom(167, 150, 214);
            newItem.addOptionRandom(297, 15, 32);
            newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
        } else if (newId == 298) {
            newItem.strOption = "327,58;380,8;2,130;0,1300";
        } else if (newId == 555) {
            newItem.strOption = "327,88;379,8;2,120;0,1200";
        } else if (newId == 431) {
            newItem.strOption = "327,48;380,8;2,140;0,1400";
        } else if (newId == 519) {
            newItem.addOptionRandom(0, 2000, 3500);
            newItem.addOptionRandom(2, 150, 315);
            newItem.addOptionRandom(5, 150, 315);
            newItem.addOptionRandom(167, 150, 214);
            newItem.addOptionRandom(297, 15, 32);
        } else if (newId == 972) {
            newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
            newItem.addOptionRandom(0, 2000, 3500);
            newItem.addOptionRandom(2, 150, 315);
            newItem.addOptionRandom(5, 150, 315);
            newItem.addOptionRandom(167, 150, 214);
            newItem.addOptionRandom(297, 15, 32);
        } else if (newId == 848) {
            newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
            newItem.addOptionRandom(0, 1000, 1500);
            newItem.addOptionRandom(2, 150, 250);
            newItem.addOptionRandom(5, 150, 250);
            newItem.addOptionRandom(167, 100, 214);
            newItem.addOptionRandom(297, 10, 25);
        } else if (newId == 374) {
            newItem.addOption(327, 89);
            newItem.addOptionRandom(379, 5, 12);
            newItem.addOptionRandom(2, 50, 160);
            newItem.addOptionRandom(0, 500, 1300);
        } else if (newId == 284) {
            newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
            newItem.addOptionRandom(0, 1000, 1500);
            newItem.addOptionRandom(2, 150, 250);
            newItem.addOptionRandom(5, 150, 250);
            newItem.addOptionRandom(167, 100, 214);
            newItem.addOptionRandom(297, 10, 25);
        } else if (newId == 309) {
            newItem.setExpire(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7));
            newItem.addOptionRandom(0, 1000, 1500);
            newItem.addOptionRandom(2, 100, 150);
            newItem.addOptionRandom(5, 100, 150);
            newItem.addOptionRandom(167, 100, 150);
            newItem.addOptionRandom(297, 10, 15);
        } else if (newId == 460) {
            newItem.strOption = "376,12,-1;379,12,-1;377,12,-1;2,500;327,62,62";
        } else if (newId == 525) {
            newItem.strOption = "327,68,68;149,1,-1;376,10,-1;152,100,-1";
        }
        if (player.addItemToBag(newItem)) {
            player.getService().addItem(newItem);
        }
        int coin = Util.nextInt(50, 100) * 1000;
        player.addCoin(coin, true);
        if (player.exp <= 230000000000L) {
            if (!player.lockEXP) {
                player.exp += Util.nextInt(100000, 150000);
                player.KI += Util.nextInt(100000, 150000);
                player.getService().showKI(player);
            }
        }
        if (item.id == 1137) {
            player.pointMakeCake += 1;
            player.updatePoint(KeyPoint.MAKE_CAKE, player.pointMakeCake);
        }
        if (item.id == 1138) {
            player.pointMakeCake += 2;
            player.updatePoint(KeyPoint.MAKE_CAKE, player.pointMakeCake);
        }
        if (item.template.id == 1146) {
            player.pointThaLongDen += 1;
            player.updatePoint(KeyPoint.THA_LONG_DEN, player.pointThaLongDen);
        }
        player.removeItem(item.index, 1, true);
    }
}
