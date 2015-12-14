package items;

public enum Rarity {
	SUPER_COMMON("Super Common", 1.15D, 1.0D), COMMON("Common", 1.20D, 1.05D), UNCOMMON("Uncommon", 1.35D, 1.1D), HEROIC("Heroic", 1.4D, 1.15D), RARE("Rare", 1.5D, 1.2D), MYTHICAL("Mythical", 1.6D, 1.25D), LEGENDARY("Legendary", 1.7D, 1.3D), EPIC("Epic", 1.8D, 1.35D), DIVINE("Divine", 1.9D, 1.4D), IMMORTAL("Immortal", 2.0D, 1.45D), SINGULARITY("Singularity", 2.5D, 1.5D);
	private final double statGrowthRate;
	private final double expGrowthRate;
	private final String rarityName;
	
	private Rarity(final String rarityName, final double statGrowthRate, final double expGrowthRate) {
		this.statGrowthRate = statGrowthRate;
		this.expGrowthRate = expGrowthRate;
		this.rarityName = rarityName;
	}
	
	public double getStatGrowthRate() {
		return statGrowthRate;
	}
	
	public double getExpGrowthRate() {
		return expGrowthRate;
	}
	
	@Override
	public String toString() {
		return this.rarityName;
	}
}
