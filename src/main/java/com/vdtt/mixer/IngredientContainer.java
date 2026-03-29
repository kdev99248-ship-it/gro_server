package com.vdtt.mixer;

import com.vdtt.model.Char;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class IngredientContainer {
    private List<Ingredient> ingredients;
    public IngredientContainer() {
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public boolean validate(Char player, int amount) {
        for (Ingredient ingredient : ingredients) {
            if(!ingredient.canDeduct(player, amount)) {
                player.serverDialog("Không đủ " + ingredient.getName());
                return false;
            }
        }
        return true;
    }

    public void deductAmounts(Char player, int amount) {
        for (Ingredient ingredient : ingredients) {
            ingredient.deduct(player, amount);
        }
    }
}
