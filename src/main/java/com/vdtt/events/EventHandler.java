package com.vdtt.events;

import com.vdtt.item.Item;
import com.vdtt.map.item.ItemMap;
import com.vdtt.map.item.ItemMapFactory;
import com.vdtt.map.zones.Zone;
import com.vdtt.mob.Mob;
import com.vdtt.model.Char;
import com.vdtt.util.Log;
import com.vdtt.util.Util;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EventHandler {

    private static ScheduledExecutorService scheduler;

    @Getter
    @Setter
    private static AbsEvent event;

    public static boolean isEvent() {
        return event != null;
    }

    public static  boolean isEvent(Class<? extends AbsEvent> eventClass) {
        if (!isEvent()) {
            return false;
        }
        return event.getClass() == eventClass;
    }

    public static void init() {
        scheduler = Executors.newScheduledThreadPool(1);
        EventType[] eventTypes = EventType.values();
        for (EventType type : eventTypes) {
            timer(type);
        }
    }

    private static void timer(EventType type) {
        ZoneId vnZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime startDate = type.getStartDate();
        LocalDateTime endDate = type.getEndDate();
        ZonedDateTime now = ZonedDateTime.now(vnZoneId);

        LocalDate today = now.toLocalDate();

        ZonedDateTime startZonedDateTime = startDate.atZone(vnZoneId);
        ZonedDateTime endZonedDateTime = endDate.atZone(vnZoneId);
        long startDelay = Duration.between(now, startZonedDateTime).toMillis();
        long endDelay = Duration.between(now, endZonedDateTime).toMillis();

        try {
            Constructor<?> constructor = type.getClazz().getConstructor();
            AbsEvent event = (AbsEvent) constructor.newInstance();
            event.setId(type.getId());
            event.setName(type.getName());
            event.setType(type);

            boolean hasEvent = false;

            if (startZonedDateTime.toLocalDate().equals(today)) {
                scheduler.schedule(event::start, startDelay, TimeUnit.MILLISECONDS);
                Log.info("Scheduled event " + type.getName() + " to start today.");
                hasEvent = true;
            } else if (startZonedDateTime.toLocalDate().isBefore(today) && endZonedDateTime.isAfter(now)) {
                hasEvent = true;
                event.start();
                Log.info("Event " + type.getName() + " is ongoing and started now.");
            }

            if (hasEvent && endZonedDateTime.toLocalDate().equals(today) && endZonedDateTime.isAfter(now)) {
                scheduler.schedule(event::end, endDelay, TimeUnit.MILLISECONDS);
                Log.info("Scheduled event " + type.getName() + " to end today.");
            }

        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            Log.error("new instance err", e);
        }
    }

    public static void dropItem(Char killer, Mob mob) {
        try {
            Zone zone = mob.zone;
            if (zone.getNumberItem() > 100) {
                return;
            }
            int dropRate = event.dropRate;
            if (Util.nextInt(100) >= dropRate) {
                return;
            }
            int itemId = event.randomItemDrop();
            Item item = new Item(itemId,"sk");
            item.setQuantity(1);
            item.expire = -1;
            item.isLock = false;
            ItemMap itemMap = ItemMapFactory.getInstance()
                    .builder()
                    .id(zone.numberDropItem++)
                    .x((short) (mob.x + Util.nextInt(-30, 50)))
                    .y((short) (mob.y))
                    .build();
            itemMap.setOwnerID(killer.id);
            itemMap.setItem(item);
            zone.addItemMap(itemMap);
            zone.getService().addItemMap(itemMap, killer);
        } catch (Exception e) {
            Log.error("dropItem", e);
        }
    }
}
