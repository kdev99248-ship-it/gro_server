package com.vdtt.handler.item;

import com.vdtt.events.SummerParty;
import com.vdtt.events.points.KeyPoint;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import com.vdtt.model.RandomItem;
import com.vdtt.util.Util;

import java.util.concurrent.TimeUnit;

public class EatCream extends UseItem {

    public EatCream() {
        super(SummerParty.class, "Vật phẩm chỉ có thể sử dụng trong sự kiện Đại tiệc mùa hè");
    }

    @Override
    protected void use(Char player, Item item) {
        if (player.getSlotNull() == 0) {
            player.warningBagFull();
            return;
        }
        int itemId = -1;
        if (item.id == 964) {
            itemId = RandomItem.ITEM_EVENT_NORMAL.next();
        } else if (item.id == 965) {
            itemId = RandomItem.ITEM_EVENT_VIP.next();
        }
        Item newItem = new Item(itemId, "sk");
        newItem.setQuantity(1);
        newItem.expire = -1;
        newItem.isLock = false;
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

        if (item.id == 964) {
            player.addEventPoint(KeyPoint.EAT_CREAM, 1);
        } else if (item.id == 965) {
            player.addEventPoint(KeyPoint.EAT_CREAM, 2);
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
        }
        player.removeItem(item.index, 1, true);
    }
}
