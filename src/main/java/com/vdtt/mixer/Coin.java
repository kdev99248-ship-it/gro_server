package com.vdtt.mixer;

import com.vdtt.model.Char;

public class Coin extends Ingredient {
    public Coin(int quantity) {
        super(-3, quantity);
    }

    @Override
    public String getName() {
        return "Xu";
    }

    @Override
    public boolean canDeduct(Char player, int amount) {
        return player.coin >= getAmount(amount);
    }

    @Override
    public void deduct(Char player, int amount) {
        int price = (int) getAmount(amount);
        player.addCoin(-price, true);
    }
}
