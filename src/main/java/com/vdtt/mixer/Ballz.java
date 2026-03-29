package com.vdtt.mixer;

import com.vdtt.model.Char;

public class Ballz extends Ingredient {
    public Ballz(int quantity) {
        super(-2, quantity);
    }

    @Override
    public String getName() {
        return "Ballz";
    }

    @Override
    public boolean canDeduct(Char player, int amount) {
        return player.ballZ >= getAmount(amount);
    }

    @Override
    public void deduct(Char player, int amount) {
        int price = (int) getAmount(amount);
        player.addBallz(-price, true);
    }
}
