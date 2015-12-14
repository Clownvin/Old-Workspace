package util;

public class BasicObject implements GameObject {
	public boolean inUse = false;
	
	@Override
	public boolean inUse() {
		return inUse;
	}

	@Override
	public void setUse(final boolean state) {
		this.inUse = state;
	}
}
