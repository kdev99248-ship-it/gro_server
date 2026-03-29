package com.vdtt.mixer;

import com.vdtt.model.Char;

public class Zeni extends Ingredient {
    public Zeni(int quantity) {
        super(-1, quantity);
    }

    @Override
    public String getName() {
        return "Zeni";
    }

    @Override
    public boolean canDeduct(Char player, int amount) {
        return player.zeni >= getAmount(amount);
    }

    @Override
    public void deduct(Char player, int amount) {
        int price = (int) getAmount(amount);
        player.addZeni(-price, true);
    }
}
