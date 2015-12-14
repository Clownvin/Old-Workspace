package com.engine.entities.items;

public enum WeaponType {
    SHORT_SWORD(0.8F, 1.2F, (byte) 2, CombatType.MELEE), LONG_SWORD(1.2F, 0.8F, (byte) 0, CombatType.MELEE), SWORD(1.0F, 1.0F, (byte) 2, CombatType.MELEE), DAGGER(0.4F, 1.6F, (byte) 2, CombatType.MELEE);
    public final float baseAttackMod;
    public final float baseSpeedMod;
    public final CombatType combatType;
    public final byte equipType;
    
    public static final byte MAIN_HAND = 0, OFF_HAND = 1, DUAL_WIELD = 2, DUAL_WEILD_REQUIRED = 3;
    
    private WeaponType(final float baseAttackMod, final float baseSpeedMod, final byte equipType, final CombatType combatType) {
	this.baseAttackMod = baseAttackMod;
	this.baseSpeedMod = baseSpeedMod;
	this.combatType = combatType;
	this.equipType = equipType;
    }
    
    public WeaponType getRandomMeleeType() {
	switch ((int)Math.random() * 4) {
	case 0:
	    return SHORT_SWORD;
	case 1:
	    return LONG_SWORD;
	case 2:
	    return SWORD;
	case 3:
	    return DAGGER;
	}
	return DAGGER;
    }
}
