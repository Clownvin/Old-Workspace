package items;

public abstract class BasicItem implements ItemInterface {
	protected final int basicID;
	protected final Rarity rarity;
	public BasicItem(final int basicID, final Rarity rarity) {
		this.basicID = basicID;
		this.rarity = rarity;
	}
	
	public int getBasicID() {
		return basicID;
	}
	
	public Rarity getRarity() {
		return rarity;
	}
}
