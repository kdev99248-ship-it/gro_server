package com.vdtt.mixer;

import com.vdtt.model.Char;

public class Enthusiastic extends Ingredient {
    public Enthusiastic(int quantity) {
        super(-4, quantity);
    }

    @Override
    public String getName() {
        return "Điểm sôi nổi";
    }

    @Override
    public boolean canDeduct(Char player, int amount) {
        return player.pointEnthusiastic >= getAmount(amount);
    }

    @Override
    public void deduct(Char player, int amount) {
        int price = (int) getAmount(amount);
        player.pointEnthusiastic -= price;
    }
}