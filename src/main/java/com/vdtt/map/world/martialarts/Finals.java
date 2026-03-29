package com.vdtt.map.world.martialarts;

import com.vdtt.model.Char;

import java.util.List;

public class Finals extends RoundOf16 {
    public Finals(MartialArtsConference conference) {
        super(conference);
        setName("Tứ kết");
    }

    @Override
    protected void consolationForLoser(List<Char> players) {
        MartialArtsConference.current.rewardVeNhi(members.get(0));
        super.consolationForLoser(players);
    }
}
