package com.vdtt.data;

import com.vdtt.skill.Skill;

import java.util.Comparator;

public final class vdtt_ia implements Comparator {

    public final int compare(Object var1, Object var2) {
        Skill var10000 = (Skill)var1;
        Skill var4 = (Skill)var2;
        Skill var3 = var10000;
        if (var10000.getSkillTemplate().i == var4.getSkillTemplate().i) {
            return var3.getSkillTemplate().levelNeed == var4.getSkillTemplate().levelNeed ? var3.getSkillTemplate().id - var4.getSkillTemplate().id : var3.getSkillTemplate().levelNeed - var4.getSkillTemplate().levelNeed;
        } else {
            return var3.getSkillTemplate().i - var4.getSkillTemplate().i;
        }
    }
}
