package com.vdtt.effect;

import com.vdtt.map.MapService;
import com.vdtt.model.Char;
import com.vdtt.network.Message;
import com.vdtt.task.TaskOrder;
import org.json.simple.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EffectManager {

    private final Char player;

    private final List<Effect> list;
    private final boolean[] statusEffectWithID;
    private final boolean[] statusEffectWithType;

    public EffectManager(Char player) {
        this.player = player;
        int size = EffectTemplateManager.getInstance().size();
        this.list = new ArrayList<>();
        this.statusEffectWithID = new boolean[size];
        this.statusEffectWithType = new boolean[size];
    }

    public void add(Effect effect) {
        synchronized (list) {
            list.add(effect);
        }
    }

    public int size() {
        return list.size();
    }

    public void remove(Effect effect) {
        synchronized (list) {
            list.remove(effect);
        }
    }

    public void setStatus(Effect eff, boolean status) {
        statusEffectWithID[eff.template.id] = status;
        statusEffectWithType[eff.template.type] = status;
    }

    public Effect findByID(int id) {
        if (id >= statusEffectWithID.length) {
            return null;
        }
        synchronized (list) {
            if (statusEffectWithID[id]) {
                for (Effect eff : list) {
                    if (eff.template.id == id&&!eff.isPet) {
                        return eff;
                    }
                }
            }
        }
        return null;
    }

    public Effect findIdExcept(Integer[] ids,int idEff) {
        synchronized (list) {
            for(Integer i : ids) {
                if (idEff != i) {
                    Effect effect = findByID(i);
                    if (effect != null) {
                        return effect;
                    }
                }
            }
        }
        return null;
    }

    public Effect findByIDPet(int id) {
        synchronized (list) {
            if (statusEffectWithID[id]) {
                for (Effect eff : list) {
                    if (eff.template.id == id && eff.isPet) {
                        return eff;
                    }
                }
            }
        }
        return null;
    }

    public Effect findByType(short type) {
        synchronized (list) {
            if (statusEffectWithType[type]) {
                for (Effect eff : list) {
                    if (eff.template.type == type&&!eff.isPet) {
                        return eff;
                    }
                }
            }
        }
        return null;
    }
    public Effect findByTypePet(byte type) {
        synchronized (list) {
            if (statusEffectWithType[type]) {
                for (Effect eff : list) {
                    if (eff.template.type == type&&eff.isPet) {
                        return eff;
                    }
                }
            }
        }
        return null;
    }

    public void update() {
        List<Effect> listExpired = filter(e -> e.isExpired());
        if (!listExpired.isEmpty()) {
            boolean isSetAbility = false;
            for (Effect eff : listExpired) {
                removeEffect(eff);
                if (player.zone != null) {
                    player.zone.getService().playerRemoveEffect(player, eff);
                }
                short type = eff.template.type;
                short effId = eff.template.id;
                if ((effId >= 85 && effId <= 130) || (effId >= 172 && effId <= 175) || effId==140 || (effId >= 168 && effId <= 169) || (effId >= 194 && effId <= 196 || effId == 148 || effId == 153)) {
                    isSetAbility = true;
                }
            }
            if (isSetAbility) {
                player.setAbility();
            }
        }
    }

    public byte[] getData() {
        return new byte[]{0};
    }

    public List<Effect> filter(Predicate<Effect> predicate) {
        synchronized (list) {
            return list.stream().filter(predicate).collect(Collectors.toList());
        }
    }

    public boolean statusWithID(int id) {
        return statusEffectWithID[id];
    }

    public boolean statusWithType(int type) {
        return statusEffectWithType[type];
    }

    public void addTime(int id, long time) {
        Effect e = findByID(id);
        if (e != null) {
            e.addTime(time);
        }
    }

    public void setEffect(Effect effect) {
        if (list.size() >= Byte.MAX_VALUE) {
            return;
        }
        effect(effect, true);
        add(effect);
        player.zone.getService().playerAddEffect(player, effect);

    }

    public void removeEffect(Effect eff) {
        effect(eff, false);
        remove(eff);
    }

    public void effect(Effect eff, boolean is) {
        int type = eff.template.type;
        int id = eff.template.id;
        if(eff.isPet){
            return;
        }
        setStatus(eff, is);
        switch (id) {
            case 117:
                if (!is && player.taskOrders != null && !player.taskOrders.isComplete()) {
                    if (player.taskOrders.taskId == TaskOrder.TASK_MAI_RUA) {
                        player.taskOrders.updateTask(1);
                    }
                }
                break;

            case 66:
                player.isProtected = is;
                break;
        }
        switch (type) {
            case 77:
                player.isSuperSayan = is;
                break;
            case 121:
                player.isLuongLongNhatThe = is;
                break;
        }
        player.setAbility();
    }

    public void clearScrAllEffect(MapService mapService, Char p) {
        synchronized (list) {
            for (Effect eff : list) {
                mapService.playerRemoveEffect(p, eff);
            }
        }
    }

    public void displayAllEffect(MapService mapService, Char p) {
        synchronized (list) {
            for (Effect eff : list) {
                if (mapService != null && p != null) {
                    mapService.playerAddEffect(p, eff);
                }
            }
        }
    }

    public JSONArray toJSONArray() {
        JSONArray effects = new JSONArray();
        synchronized (list) {
            for (Effect eff : list) {
                int type = eff.template.type;
                int effId = eff.template.id;
                if(effId == 117){
                    effects.add(eff.toJSONObject());
                }
                if ((effId >= 85 && effId <= 130) || (effId >= 172 && effId <= 175) || effId == 59 || effId == 136 || effId == 137 || effId == 138||effId == 140) {
                    continue;
                }
                effects.add(eff.toJSONObject());

            }
        }
        return effects;
    }

    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    public EffectManager clone(Char p) {
        EffectManager em = new EffectManager(p);
        synchronized (list) {
            list.forEach(e -> {
                Effect newEffect = new Effect(e.template.id, e.getStartAt(), e.getEndAt(), e.getParam());
                newEffect.param2 = e.getParam2();
                em.add(newEffect);
            });
        }
        return em;
    }

    private int getIndexById(byte id) {
        int index = 0;
        synchronized (list) {
            for (Effect e : list) {
                if (e.template.id == id) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    private int getIndexByType(byte type) {
        int index = 0;
        synchronized (list) {
            for (Effect e : list) {
                if (e.template.type == type) {
                    return index;
                }
                index++;
            }
        }
        return -1;
    }

    public void write(Message m) {
        try {
            synchronized (list) {
                m.writer().writeByte(list.size());
                for (Effect e : list) {
                    e.write(m);
                }
            }
        } catch (IOException e) {

        }
    }
}
