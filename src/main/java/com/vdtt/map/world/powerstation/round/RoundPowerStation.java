package com.vdtt.map.world.powerstation.round;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.World;
import com.vdtt.map.world.martialarts.ZMartialArts;
import com.vdtt.map.world.powerstation.PowerStation;
import com.vdtt.map.world.powerstation.ZPowerTournament;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import com.vdtt.party.Group;
import com.vdtt.skill.Skill;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.vdtt.map.world.powerstation.PowerStation.current;

public abstract class RoundPowerStation extends World {
    private final PowerStation powerStation;
    protected int maxGroupZone;
    @Setter
    private int prepareTime;
    @Getter
    private boolean fighting;

    public RoundPowerStation(PowerStation powerStation, String name, int maxGroupZone) {
        this.powerStation = powerStation;
        this.name = name;
        generateId();
        this.maxGroupZone = maxGroupZone;
        this.initFinished = true;
        members = new ArrayList<>();
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof ZPowerTournament) && next instanceof ZPowerTournament;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof ZPowerTournament && !(next instanceof ZPowerTournament);
    }

    @Override
    public void doFinish() {
        this.isClosed = true;
    }

    @Override
    public void close() {
        this.createdAt = System.currentTimeMillis();
        this.isClosed = true;
        doEnd();
    }

    @Override
    public void doEnd() {
        nextRound();
    }

    @Override
    public void update() {
//        if (MartialManager.isStart()) {
        super.update();
        long now = System.currentTimeMillis();
        if (!fighting) {
            if (now - createdAt > prepareTime) {
                startFighting();
            }
        }
//        }
    }

    private void startFighting() {
        fighting = true;
        if (current == null) return;
        for (Group group : current.getList()) {
            for (Char player : group.getChars()) {
                player.setTypePk((byte) 3);
            }
        }
    }

    private void nextRound() {
        if (current == null) return;
        current.start = true;
        int numberOfTeams = current.getList().size() / 2;
        if (current.getList().size() % 2 != 0) {
            numberOfTeams += 1;
        }
        nextRound(numberOfTeams, new ArrayList<>(current.getList()));
    }

    protected void createArea() {
        Map map = MapManager.getInstance().find(48);
        ZPowerTournament area = new ZPowerTournament(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    private void nextRound(int numberOfTeams, List<Group> groups) {
        List<Group> leaders = groups.stream().filter(group -> !group.getCharsInMap(48).isEmpty()).sorted((group, group1) -> group1.powerStationPoint - group.powerStationPoint).limit(numberOfTeams).toList();
        List<Group> remaining = new ArrayList<>(groups);
        remaining.removeAll(leaders);
        consolationForLoserGroup(remaining);
        powerStation.nextRound(numberOfTeams, leaders);
    }

    @Override
    public void addMember(Char _char) {
    }

    @Override
    public void removeMember(Char _char) {
    }

    public void removeGroup(Group group) {
        if (current != null)
            current.removeGroup(group);
    }

    public void join(Char player) {
        player.addWorld(this);
        player.getGroup().powerStationPoint = 0;
        player.countWakeUpFromDead = 0;
        player.setTypePk((byte) 0);
        player.wakeUpFromDead();
        for (Skill skill : player.skills) {
            skill.timeCoolDown = 0;
        }
        player.getService().updateSkill();
        for (Zone zone : zones) {
            Set<Group> groupHashSet = new HashSet<>();
            for (Char _char : zone.getChars()) {
                groupHashSet.add(_char.getGroup());
            }
            if (groupHashSet.contains(player.getGroup()) || groupHashSet.size() < maxGroupZone) {
                zone.join(player, -1);
                return;
            }
        }
        createArea();
        zones.get(zones.size() - 1).join(player, -1);
    }

    protected abstract void consolationForLoserGroup(List<Group> groups);
}
