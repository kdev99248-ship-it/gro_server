package com.vdtt.mixer;

import com.vdtt.item.Item;

public class Product extends Ingredient {
    private boolean locked;

    public Product(int itemID, int quantity) {
        super(itemID, quantity);
    }

    public Product(int itemID, int quantity, boolean locked) {
        super(itemID, quantity);
        this.locked = locked;
    }

    public Item createProduct(int amount) {
        Item item = new Item(itemID,"product");
        item.isLock = locked;
        item.setQuantity((int) getAmount(amount));
        return item;
    }
}