package com.engine.entities.items;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public final class WeaponStats {
    
    public static final int ATTACK = 0, STRENGTH = 1, DEXTERITY = 2, TOUGHNESS = 3, VITALITY = 4, HEALING = 5;
    private int[] attributes;
    private double[] levelingMods;
    
    private WeaponStats(final int[] attributes, final double[] levelingMods) {
	this.attributes = attributes;
	this.levelingMods = levelingMods;
    }
    
    public static WeaponStats generateStats(int level, ItemRarity rarity) {
	//double base = ((level * ((level * 0.025D )* rarity.statGenMod)) * 6) + 10;
	double base = (20 + (5 * (level - 1))) * rarity.statGenMod;
	int[] attributes = new int[6];
	double[] levelingMods = new double[6];
	double segment = base / 6.0D;
	for (int i = 0; i < 6; i++) {
	    attributes[(int) (Math.random() * 6)] += segment;
	}
	for (int i = 0; i < 6; i++) {
	    levelingMods[i] = ((1.0D + ((attributes[i] / (base * 2))/1.75D)));
	}
	return new WeaponStats(attributes, levelingMods);
    }
    
    public int getAttribute(int attribute) {
	if (attribute < 0 || attribute > 5) {
	    throw new IllegalArgumentException("Attribute must be between 0 and 5.");
	}
	return attributes[attribute];
    }
    
    public static void main(String[] args) {
	for(int i = 1; i < 451; i++) {
	    System.out.println("Level "+i);
	    WeaponStats s = generateStats(i, ItemRarity.COMMON);
	    System.out.println(s.getAttribute(ATTACK)+"; Mod: "+((int)(s.levelingMods[ATTACK] * 100) - 100));
	    System.out.println(s.getAttribute(STRENGTH)+"; Mod: "+((int)(s.levelingMods[STRENGTH] * 100) - 100));
	    System.out.println(s.getAttribute(DEXTERITY)+"; Mod: "+((int)(s.levelingMods[DEXTERITY] * 100) - 100));
	    System.out.println(s.getAttribute(TOUGHNESS)+"; Mod: "+((int)(s.levelingMods[TOUGHNESS] * 100) - 100));
	    System.out.println(s.getAttribute(VITALITY)+"; Mod: "+((int)(s.levelingMods[VITALITY] * 100) - 100));
	    System.out.println(s.getAttribute(HEALING)+"; Mod: "+((int)(s.levelingMods[HEALING] * 100) - 100));
	    System.out.println("");
	}
	int[] s = new int[6];
	for (int i = 0; i < 6 * 20000; i++) {
	    s[(int) (Math.random() * 6)]++;
	}
	for (int i = 0; i < 6; i++) {
	    System.out.println(s[i] +", percent off: "+((s[i] - 20000)/20000.0D) * 100);
	}
    }
}
