package com.vdtt.model;

import com.vdtt.item.Item;
import com.vdtt.util.Log;
import com.vdtt.util.RandomCollection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class QuaySo {
    private RandomCollection<ItemQuaySo> items;
    public QuaySo() {
        items = new RandomCollection<>();
        init();
    }

    private void init() {
        add(ItemQuaySo.builder().id(0).idItem(0).quantity(1).rate(80).build());
        add(ItemQuaySo.builder().id(1).idItem(1).quantity(3).rate(1).build());
        add(ItemQuaySo.builder().id(2).idItem(1).quantity(1).rate(20).build());
        add(ItemQuaySo.builder().id(3).idItem(682).quantity(1).rate(0.1).build());
        add(ItemQuaySo.builder().id(4).idItem(0).quantity(2).rate(1).build());
        add(ItemQuaySo.builder().id(5).idItem(1).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(6).idItem(3).quantity(3).rate(0.5).build());
        add(ItemQuaySo.builder().id(7).idItem(3).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(8).idItem(2).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(9).idItem(0).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(10).idItem(1).quantity(2).rate(1).build());
        add(ItemQuaySo.builder().id(11).idItem(3).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(12).idItem(2).quantity(3).rate(0.5).build());
        add(ItemQuaySo.builder().id(13).idItem(2).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(14).idItem(177).quantity(1).rate(0.1).build());
        add(ItemQuaySo.builder().id(15).idItem(3).quantity(2).rate(1).build());
        add(ItemQuaySo.builder().id(16).idItem(2).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(17).idItem(0).quantity(3).rate(0.5).build());
        add(ItemQuaySo.builder().id(18).idItem(0).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(19).idItem(1).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(20).idItem(3).quantity(1).rate(40).build());
        add(ItemQuaySo.builder().id(21).idItem(2).quantity(2).rate(1).build());
    }

    protected void add(ItemQuaySo card) {
        items.add(card.getRate(), card);
    }

    protected boolean isCanSpin(@NotNull Char p, byte type) {
//        if(p.itemQuaySo != null){
//            return true;
//        }
        if (p.user.activated == 1) {
            p.serverDialog("Tính năng cần mở thành viên ở bulma");
            return false;
        }
        if(!p.isActiveAction()) {
            p.serverDialog("Vui lòng mở mã bảo vệ để tiếp tục");
            return false;
        }
        int quantity = p.getQuantityItemById(5000);//id quay so 176
        int quantityNeed = 2 * (type == 0 ? 1 : type == 1 ? 5 : type == 2 ? 25 : 50);
        if (quantity < quantityNeed) {
            p.getService().serverMessage("Chức Năng Đang Nâng Cấp Sò Lông");
            return false;
        }
        return true;
    }

    public void spin(@NotNull Char p, byte type) {
        if (isCanSpin(p, type)) {
            ItemQuaySo result = items.next();
            if (p.itemQuaySo == null) {
                int quantityNeed = 2 * (type == 0 ? 1 : type == 1 ? 5 : type == 2 ? 25 : 50);
                int index = p.getIndexItemByIdInBag(5000,true);
                int index2 = p.getIndexItemByIdInBag(5000,false);
                List<Item> items =new ArrayList<>();
                if(index != -1)
                    items.add(p.bag[index]);
                if(index2 != -1)
                    items.add(p.bag[index2]);
                p.removeItems(5000,quantityNeed);
                p.itemQuaySo = ItemQuaySo.builder().id(result.getId()).idItem(result.getIdItem()).quantity(result.getQuantity() - 1).type(type).build();
                p.getService().startSpin(items);
            } else {
                if (p.itemQuaySo.getQuantity() > 2) {
                    int i = 0;
                    while (i < 10&& result.getIdItem() == p.itemQuaySo.getIdItem()) {
                        result= items.next();
                    }
                }
                if(p.itemQuaySo.getIdItem() == result.getIdItem()) {
                    p.itemQuaySo.setQuantity(p.itemQuaySo.getQuantity() + result.getQuantity());
                    if(p.itemQuaySo.getQuantity() >6){
                        p.itemQuaySo.setQuantity(6);
                    }
                    p.getService().spin();
                }else if(result.getIdItem()==177||result.getIdItem()==682) {
                    p.itemQuaySo = ItemQuaySo.builder().id(result.getId()).idItem(result.getIdItem()).quantity(result.getQuantity() - 1).type(type).build();
                    p.getService().spin();
                }else {
                    p.itemQuaySo = null;
                    p.getService().spinLoss(result.getId(), result.getIdItem());
                }
            }
        }
    }


}
