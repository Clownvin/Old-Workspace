package items;

public class BasicWeapon extends BasicItem implements ItemInterface {
	protected String itemName;
	/**         % DEAL DMG, MAX DMG,  **/
	protected int sharpness, brutality;
	protected int intellect, willpower;
	protected int precision, drawStrength;
	protected int accuracy;
	
	protected final WeaponType weaponType;
	
	public BasicWeapon(final int basicID, String itemName, final Rarity rarity, final WeaponType weaponType) {
		super(basicID, rarity);
		this.itemName = itemName;
		this.weaponType = weaponType;
	}
	
	public String getName() {
		return itemName;
	}
	
	public int getSharpness() {
		return sharpness;
	}
	
	public int getBrutality() {
		return brutality;
	}
	
	public int getIntellect() {
		return intellect;
	}
	
	public int getWillpower() {
		return willpower;
	}
	
	public int getPrecision() {
		return precision;
	}
	
	public int getDrawStrength() {
		return drawStrength;
	}
	
	public int getAccuracy() {
		return accuracy;
	}
}
