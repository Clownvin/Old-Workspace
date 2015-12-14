package items;

public class NamedWeapon extends BasicWeapon implements LivingItem {
	protected int currentExp = 0;
	protected int expThisLevel = 50;
	protected int level = 1;

	public NamedWeapon(final int basicID, String itemName, final Rarity rarity, final WeaponType weaponType) {
		super(basicID, itemName, rarity, weaponType);
	}

	protected void levelUp() {
		if (currentExp >= expThisLevel) {
			currentExp = currentExp % expThisLevel;
			level++;
			expThisLevel = (int) ((((double) (level * level) / 2) * 100) * rarity
					.getExpGrowthRate());
			switch(weaponType.getDamageType()) {
			case MAGIC:
				intellect *= rarity.getStatGrowthRate();
				intellect += (int) rarity.getStatGrowthRate();
				willpower *= rarity.getStatGrowthRate();
				willpower += (int) rarity.getStatGrowthRate();
				break;
			case MELEE:
				sharpness *= rarity.getStatGrowthRate();
				sharpness += (int) rarity.getStatGrowthRate();
				brutality *= rarity.getStatGrowthRate();
				brutality += (int) rarity.getStatGrowthRate();
				break;
			case RANGED:
				precision *= rarity.getStatGrowthRate();
				precision += (int) rarity.getStatGrowthRate();
				drawStrength *= rarity.getStatGrowthRate();
				drawStrength += (int) rarity.getStatGrowthRate();
				break;
			default:
				accuracy *= rarity.getStatGrowthRate();
				accuracy += (int) rarity.getStatGrowthRate();
				break;
			
			}
		}
	}

	@Override
	public int getExp() {
		return currentExp;
	}

	@Override
	public void addExp(int exp) {
		currentExp += exp;
		if (currentExp > expThisLevel)
			levelUp();
	}

	@Override
	public int getLevel() {
		return level;
	}
}
