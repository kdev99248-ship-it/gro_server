package com.vdtt.achievement;

import com.vdtt.item.Item;
import com.vdtt.util.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Achievement {
    private int id;
    private String name;
    private int amount;
    private int exp;
    private int gold, silver, bronze;
    private String item;

    public Item toItem() {
        if (this.item == null) {
            return null;
        }

        return Util.parseItem(this.item);
    }
}
