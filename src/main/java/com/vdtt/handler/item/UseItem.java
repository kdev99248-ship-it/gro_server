package com.vdtt.handler.item;

import com.vdtt.events.AbsEvent;
import com.vdtt.events.EventHandler;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public abstract class UseItem {

    private Class<? extends AbsEvent> event;
    private String eventWarning;

    public void doUse(Char player, Item item) {
        if (event != null && !EventHandler.isEvent(event)) {
            player.serverDialog(eventWarning);
            return;
        }
        use(player, item);
    }

    protected abstract void use(Char player, Item item);
}
