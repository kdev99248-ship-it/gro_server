package com.vdtt.mixer;

import com.vdtt.item.Item;
import com.vdtt.item.ItemManager;
import com.vdtt.model.Char;
import com.vdtt.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Ingredient {
    protected int itemID;
    protected int amount;

    public String getName() {
        return ItemManager.getInstance().itemTemplates.get(itemID).name;
    }

    public long getAmount(long amount) {
        return this.amount * amount;
    }

    public boolean canDeduct(Char player, int amount) {
        int needQuantity = (int) getAmount(amount);
        if (needQuantity == 1) {
            Item item = player.getItemInBag(itemID);
            if (item != null) {
                return item.has(needQuantity);
            }
        } else {
            List<Item> list = player.getBagListById(itemID);
            int sum = 0;
            for (Item item : list) {
                sum += item.getQuantity();
                if (sum >= needQuantity) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deduct(Char player, int amount) {
        int needQuantity = (int) getAmount(amount);
        if (needQuantity == 1) {
            Item item = player.getItemInBag(itemID);
            if (item != null) {
                player.removeItem(item.index, needQuantity, true);
            }
        } else {
            List<Item> list = player.getBagListById(itemID);
            for (Item item : list) {
                if (item.has(needQuantity)) {
                    player.removeItem(item.index, needQuantity, true);
                    return;
                } else {
                    int haveQuantity = item.getQuantity();
                    needQuantity -= haveQuantity;
                    player.removeItem(item.index, haveQuantity, true);
                }
            }
        }
    }

    @Override
    public String toString() {
        if (amount > 1) {
            return String.format("%s %s", Util.formatMoney(amount), getName());
        }
        return getName();
    }
}