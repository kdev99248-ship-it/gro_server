package com.vdtt.reward;

import com.vdtt.item.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Reward {
    @Setter
    @Getter
    public int id;
    @Getter
    @Setter
    private String name;
    public int type;
    public Item item;
    public int required;
}
