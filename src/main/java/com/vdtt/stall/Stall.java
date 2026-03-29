package com.vdtt.stall;

import com.vdtt.item.Item;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Stall {
    private int id;
    private String name;
    private int time;
    private int price;
    private Item item;
    private byte status;

    public boolean isExpired() {
        return time <= System.currentTimeMillis() / 1000;
    }
}
