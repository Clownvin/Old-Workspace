package com.hexane.gamemechanics;

public enum DamageType {
	MELEE("Melee"), MAGIC("Magic"), RANGED("Ranged"), BURN("Burn"), POISON("Poison"), FROSTBITE("Frostbite"), BLEEDING("Bleeding");
	
	private final String damageName;
	
	private DamageType(final String damageName) {
		this.damageName = damageName;
	}
	
	@Override
	public String toString() {
		return damageName;
	}
}
