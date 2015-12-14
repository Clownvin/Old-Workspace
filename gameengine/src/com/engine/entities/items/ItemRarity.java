package com.engine.entities.items;

public enum ItemRarity {
    COMMON(0.5), UNCOMMON(.6), VERY_UNCOMMON(.7), SUPER_UNCOMMON(.75), RARE(.8), VERY_RARE(.85), SUPER_RARE(.9), EXOTIC(1), LEGENDARY(1.5), MYTHIC(2), DIVINE(5), DEVELOPER(10);
    public final double statGenMod;
    
    private ItemRarity(final double statGenMod) {
	this.statGenMod = statGenMod;
    }
}
