package com.vdtt.mixer;

import com.vdtt.item.ItemManager;
import com.vdtt.item.ItemTemplate;
import com.vdtt.model.Char;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Getter
public class ProductContainer {
    private List<Product> products;
    public ProductContainer() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public boolean validate(Char player, int amount) {
        if (calculateEmptySlots(amount) > player.getSlotNull()) {
            player.serverDialog("Hành trang đã đầy!");
            return false;
        }
        return true;
    }

    public int calculateEmptySlots(int amount) {
        int emptySlots = 0;
        for (Product product : products) {
            int itemID = product.getItemID();
            ItemTemplate template = ItemManager.getInstance().getItemTemplate(itemID);
            if (template.isXepChong) {
                emptySlots += 1;
            } else {
                emptySlots += (int) product.getAmount(amount);
            }
        }
        return emptySlots;
    }
}
