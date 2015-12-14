package items;

import com.hexane.gamemechanics.DamageType;

public enum WeaponType {
	RAPIER("Rapier", DamageType.MELEE), LONGSWORD("Longsword", DamageType.MELEE), KATANA("Katana", DamageType.MELEE), GREATSWORD("Greatsword", DamageType.MELEE), GREATAXE("Greataxe", DamageType.MELEE), BATTLEAXE("Battleaxe", DamageType.MELEE), SHORTSWORD("Shortsword", DamageType.MELEE), DAGGER("Dagger", DamageType.MELEE),
	WAND("Wand", DamageType.MAGIC), STAFF("Staff", DamageType.MAGIC), BATTLESTAFF("Battlestaff", DamageType.MAGIC), BOOK("Book", DamageType.MAGIC), FOCUS("Focus", DamageType.MAGIC), SCEPTER("Scepter", DamageType.MAGIC), 
	SHORTBOW("Shortbow", DamageType.RANGED), LONGBOW("Longbow", DamageType.RANGED), CROSSBOW("Crossbow", DamageType.RANGED), COMPOUND_BOW("Compound Bow", DamageType.RANGED);
	
	private final String weaponName;
	private final DamageType damageType;
	
	private WeaponType(final String weaponName, final DamageType damageType) {
		this.weaponName = weaponName;
		this.damageType = damageType;
	}
	
	@Override
	public String toString() {
		return weaponName;
	}
	
	public DamageType getDamageType() {
		return damageType;
	}
}
