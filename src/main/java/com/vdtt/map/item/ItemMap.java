package com.vdtt.map.item;

import com.vdtt.item.Item;
import com.vdtt.network.Message;
import com.vdtt.util.TimeUtils;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Getter
@Setter
public class ItemMap {
    protected short id;
    protected Item item;
    protected int ownerID;
    protected short x;
    protected short y;
    protected boolean pickedUp;
    protected int requireItemID = -1;
    protected long createdAt;
    protected long expired = 30000;

    public Lock lock = new ReentrantLock();

    public ItemMap(short id) {
        this.id = id;
        this.createdAt = System.currentTimeMillis();
    }

    public boolean isExpired() {
        return TimeUtils.canDoWithTime(createdAt, expired);
    }

    public boolean isCanPickup() {
        return TimeUtils.canDoWithTime(createdAt, 20000);
    }

    public int getItemID() {
        return item.id;
    }

    public void write(Message m) throws IOException {
        m.writer().writeInt(ownerID);
        m.writer().writeShort(getId());
        m.writer().writeShort(x);
        m.writer().writeShort(y);
        item.write(m);
    }
}
