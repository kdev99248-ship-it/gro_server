package com.vdtt.handler.item;

import com.vdtt.item.Item;
import com.vdtt.item.ItemOption;
import com.vdtt.model.Char;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class KICloud extends UseItem {
    @Override
    protected void use(Char player, Item item) {
        Item cloud = player.body[10];
        if (cloud == null || !cloud.isCloud()) {
            player.serverDialog("Hãy trang bị Cân đẩu vân.");
            return;
        }
        if (!cloud.isForever()) {
            player.serverDialog("Chỉ có thể sử dụng cho Cân đẩu vân vĩnh viễn");
            return;
        }
        ItemOption powerOption = null;
        ItemOption[] options = cloud.options();
        for (ItemOption option : options) {
            if (option.getId() == 413) {
                powerOption = option;
                break;
            }
        }
        if (powerOption == null) {
            powerOption = new ItemOption(413, 0, 30000);
            List<ItemOption> newOptions = new ArrayList<>(List.of(options));
            newOptions.add(0, powerOption);
            options = newOptions.toArray(ItemOption[]::new);

        }
        if (powerOption.getParam() >= powerOption.getParam2()) {
            if(powerOption.getParam() > powerOption.getParam2()) {
                powerOption.setValue(powerOption.getParam2());
                cloud.strOption = Item.creatOption(options);
                player.getService().updateItemBody(cloud);
            }
            player.serverDialog("Cân đẩu vân đã đạt sức mạnh tối đa, không thể nâng thêm.");
            return;
        }
        powerOption.addValue(10);
        if (powerOption.getParam() >= powerOption.getParam2()) {
            powerOption.setValue(powerOption.getParam2());
        }
        cloud.strOption = Item.creatOption(options);
        player.removeItem(item.index, 1, true);
        player.getService().updateItemBody(cloud);
    }

    public static void upPower(Char player, int ki) {
        Item cloud = player.body[10];
        if (cloud == null || !cloud.isCloud() || ki <= 0) {
            return;
        }
        if (!cloud.isForever()) {
            return;
        }
        ItemOption powerOption = null;
        if ((powerOption = cloud.getOption(413)) == null)
            return;
        
        if (powerOption.getParam() >= powerOption.getParam2()) {
            return;
        }
        
        powerOption.addValue(ki);
        if (powerOption.getParam() >= powerOption.getParam2()) {
            powerOption.setValue(powerOption.getParam2());
        }
        cloud.replaceOptionById(powerOption);
        player.getService().updateItemBody(cloud);
    }
}
 