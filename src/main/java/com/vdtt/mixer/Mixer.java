package com.vdtt.mixer;

import com.vdtt.data.CMDInputDialog;
import com.vdtt.item.Item;
import com.vdtt.model.Char;
import com.vdtt.model.InputDialog;
import lombok.Getter;

import java.util.List;

@Getter
public class Mixer {

    private static final int MAX_AMOUNT = 1000;

    public static void mix(Char receiver, IngredientContainer ingredientContainer, ProductContainer productContainer, boolean isShowInputDialog) {
        if(isShowInputDialog) {
            InputDialog inputDialog = new InputDialog(CMDInputDialog.EXECUTE, "Nhập số lượng", () -> {
                InputDialog input = receiver.getInput();
                try {
                    int amount = Integer.parseInt(input.getText());
                    createProduct(receiver, ingredientContainer, productContainer, amount);
                } catch (NumberFormatException e) {
                    receiver.serverDialog("Số lượng không hợp lệ!");
                }
            });
            receiver.setInput(inputDialog);
            receiver.getService().showInputDialog();
        } else {
            createProduct(receiver, ingredientContainer, productContainer, 1);
        }
    }

    public static void mix(Char receiver, IngredientContainer ingredientContainer, ProductContainer productContainer) {
        mix(receiver, ingredientContainer, productContainer, false);
    }

    private static void createProduct(Char player, IngredientContainer ingredientContainer, ProductContainer productContainer, int amount) {
        if (validate(player, ingredientContainer, productContainer, amount)) {
            ingredientContainer.deductAmounts(player, amount);
            deliveryItemsToPlayer(player, productContainer, amount);
        }
    }

    private static void deliveryItemsToPlayer(Char player, ProductContainer productContainer, int amount) {
        List<Product> products = productContainer.getProducts();
        for (Product product : products) {
            Item item = product.createProduct(amount);
            player.addItemToBag(item);
            player.getService().addItem(item);
        }
    }

    private static boolean validate(Char player, IngredientContainer ingredientContainer, ProductContainer productContainer, int amount) {
        if (amount < 1 || amount > MAX_AMOUNT) {
            player.serverDialog("Số lượng tối thiểu là 1 và tối đa là " + MAX_AMOUNT);
            return false;
        }
        return ingredientContainer.validate(player, amount) && productContainer.validate(player, amount);
    }

}
