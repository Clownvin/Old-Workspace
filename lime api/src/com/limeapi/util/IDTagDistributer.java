package com.limeapi.util;

public final class IDTagDistributer {
    public class IDTag {
	private final int id;
	private final IDTagDistributer distributer;
	private IDTag(final int id, final IDTagDistributer distributer) {
	    this.id = id;
	    this.distributer = distributer;
	}
	
	public int getId() {
	    return id;
	}
	
	public void returnToDistributer() {
	    if (distributer == null) {
		throw new RuntimeException("IDTag's distributer is null.");
	    }
	    distributer.returnTag(this);
	}
    }
    private final CyclicArrayList<IDTag> tags;
    
    public IDTagDistributer(int tagAmount) {
	System.out.println(tagAmount);
	tags = new CyclicArrayList<IDTag>(tagAmount);
	for (int i = 0; i < tags.getCapacity(); i++) {
	    try {
		tags.add(new IDTag(i, this));
	    } catch (MaximumCapacityReachedException e) {
		e.printStackTrace();
	    }
	}
    }
    
    public IDTag getTag() {
	return tags.removeNext();
    }
    
    private void returnTag(IDTag tag) {
	try {
	    tags.add(tag);
	} catch (MaximumCapacityReachedException e) {
	    e.printStackTrace();
	}
    }
}
