package com.vdtt.skill;

import com.vdtt.data.DataCenter;
import com.vdtt.data.vdtt_ia;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class SkillFactory {
    private static final SkillFactory instance = new SkillFactory();

    public static SkillFactory getInstance() {
        return instance;
    }
    public static final Comparator a = new vdtt_ia();

    public Skill newSkill(int id, int point) {
        getSkill(id, point);
        return null;
    }

    public Skill getSkill(int id, int point) {
        for (Skill skill : DataCenter.gI().skill) {
            if (skill != null && skill.getSkillTemplate().id == id && skill.level == point) {
                return skill.clone();
            }
        }
        return null;
    }
    public Skill getSkill(int id, int point,byte classId) {
        for (Skill skill : DataCenter.gI().skill) {
            if (skill != null && skill.getSkillTemplate().id == id && skill.level == point&&skill.getSkillTemplate().idChar==classId) {
                return skill.clone();
            }
        }
        return null;
    }
    public List<Skill>getSkillByLevelNeed(int level){
        List<Skill>skills=new ArrayList<>();
        for (Skill skill : DataCenter.gI().skill) {
            if (skill != null && skill.getSkillTemplate().levelNeed == level&&skill.level == 1) {
                skills.add(skill.clone());
            }
        }
        return skills;
    }

    public Vector<Skill> getSkillByClass() {
        Vector<Skill> skills = new Vector<>();
        skills.add(getSkill(SkillName.THE_CHAT_TIEM_NANG, 0));
        skills.add(getSkill(SkillName.NHANH_NHEN_TIEM_NANG, 0));
        skills.add(getSkill(SkillName.TINH_THAN_TIEM_NANG, 0));
        skills.add(getSkill(SkillName.SUC_KHOE_TIEM_NANG, 0));
        skills.add(getSkill(SkillName.NANG_LUONG_TIEM_NANG, 0));
        skills.add(getSkill(SkillName.DANH_CO_BAN, 1));
        return skills;
    }

    public SkillTemplate getSkillTemplate(int id) {
        for (SkillTemplate skillTemplate : DataCenter.gI().skillTemplate) {
            if (skillTemplate != null && skillTemplate.id == id) {
                return skillTemplate;
            }
        }
        return null;
    }
    public int getIdSkillByName(String name){
        for (SkillTemplate skillTemplate : DataCenter.gI().skillTemplate) {
            if (skillTemplate != null && skillTemplate.name.toLowerCase().contains(name.toLowerCase())) {
                return skillTemplate.id;
            }
        }
        return -1;
    }
    public int getIdSkillByName(String name,byte classId){
        for (SkillTemplate skillTemplate : DataCenter.gI().skillTemplate) {
            if (skillTemplate != null && skillTemplate.name.toLowerCase().contains(name.toLowerCase())&&skillTemplate.idChar==classId) {
                return skillTemplate.id;
            }
        }
        return -1;
    }
}
