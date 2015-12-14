package com.engine.gameutils;

import com.engine.util.CyclicArrayList;
import com.engine.util.MaximumCapacityReachedException;

public final class IDTagDistributer {
    public class IDTag {
	private final int id;
	private final IDTagDistributer distributer;
	protected IDTag(final int id, final IDTagDistributer distributer) {
	    this.id = id;
	    this.distributer = distributer;
	}
	
	public int getId() {
	    return id;
	}
	
	public void returnToDistributer() {
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
    
    protected void returnTag(IDTag tag) {
	try {
	    tags.add(tag);
	} catch (MaximumCapacityReachedException e) {
	    e.printStackTrace();
	}
    }
}
