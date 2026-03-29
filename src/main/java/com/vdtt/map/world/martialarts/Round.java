package com.vdtt.map.world.martialarts;

import com.vdtt.map.Map;
import com.vdtt.map.MapManager;
import com.vdtt.map.world.World;
import com.vdtt.map.zones.Zone;
import com.vdtt.model.Char;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.vdtt.map.world.martialarts.MartialArtsConference.*;

public abstract class Round extends World {
    private final MartialArtsConference conference;
    protected int maxPlayersInZone;
    @Setter
    private int prepareTime;
    @Getter
    private boolean fighting;

    public Round(MartialArtsConference conference, String name, int maxPlayersInZone) {
        this.conference = conference;
        this.name = name;
        generateId();
        this.maxPlayersInZone = maxPlayersInZone;
        this.initFinished = true;
        members = new ArrayList<>();
    }

    @Override
    public boolean enterWorld(Zone pre, Zone next) {
        return !(pre instanceof ZMartialArts) && next instanceof ZMartialArts;
    }

    @Override
    public boolean leaveWorld(Zone pre, Zone next) {
        return pre instanceof ZMartialArts && !(next instanceof ZMartialArts);
    }

    @Override
    public void doFinish() {
        this.isClosed = true;
    }

    @Override
    public void close() {
        this.createdAt = System.currentTimeMillis();
        doEnd();
    }

    @Override
    public void doEnd() {
        nextRound();
    }

    @Override
    public void update() {
        if (MartialManager.isStart()) {
            super.update();
            long now = System.currentTimeMillis();
            if (!fighting) {
                if (now - createdAt > prepareTime) {
                    startFighting();
                }
            }

        }
    }

    private void startFighting() {
        fighting = true;
        for (Char player : current.getList()) {
            player.setTypePk((byte) 3);
        }
    }

    private void nextRound() {
        current.round++;
        switch (current.round) {
            case 1: {
                nextRound(16, new ArrayList<>(current.getList()));
                break;
            }
            case 2: {
                nextRound(8, new ArrayList<>(current.getList()));
                break;
            }
            case 3: {
                nextRound(4, new ArrayList<>(current.getList()));
                break;
            }
            case 4: {
                nextRound(2, new ArrayList<>(current.getList()));
                break;
            }
            case 5: {
                nextRound(1, new ArrayList<>(current.getList()));
                break;
            }
        }
    }

    protected void createArea() {
        Map map = MapManager.getInstance().find(50);
        ZMartialArts area = new ZMartialArts(this.id, map.mapTemplate, map);
        area.setWorld(this);
        area.worldMaxTime = maxTime;
        area.worldCreatedAt = createdAt;
        area.timeInMap = false;
        addZone(area);
    }

    private void nextRound(int numberOfTeams, List<Char> members) {
        List<Char> leaders = members.stream().filter(member -> member != null && member.zone instanceof ZMartialArts).sorted((o1, o2) -> o2.martialArtsPoint - o1.martialArtsPoint).limit(numberOfTeams).toList();
        if (leaders.size() < 16) {
            numberOfTeams = 8;
            current.round = 2;
        }
        if (leaders.size() < 8) {
            numberOfTeams = 4;
            current.round = 3;
        }
        if (leaders.size() < 4) {
            numberOfTeams = 2;
            current.round = 4;
        }
        if (leaders.size() <= 2) {
            numberOfTeams = 1;
            current.round = 5;
        }
        leaders = members.stream().filter(member -> member.zone instanceof ZMartialArts).sorted((o1, o2) -> o2.martialArtsPoint - o1.martialArtsPoint).limit(numberOfTeams).toList();
        List<Char> remaining = new ArrayList<>(members);
        remaining.removeAll(leaders);
        consolationForLoser(remaining);
        conference.nextRound(numberOfTeams, leaders);
    }

    @Override
    public void addMember(Char _char) {
        if (current != null) current.addMember(_char);
    }

    @Override
    public void removeMember(Char _char) {
        if (current != null)
            current.removeMember(_char);
    }

    public synchronized void join(Char player) {
        player.addWorld(this);
        player.martialArtsPoint = 0;
        player.setTypePk((byte) 0);
        player.wakeUpFromDead();
        for (Zone zone : zones) {
            if (zone.getChars().size() < maxPlayersInZone) {
                zone.join(player, -1);
                return;
            }
        }
        createArea();
        zones.get(zones.size() - 1).join(player, -1);
    }

    protected abstract void consolationForLoser(List<Char> players);
}
