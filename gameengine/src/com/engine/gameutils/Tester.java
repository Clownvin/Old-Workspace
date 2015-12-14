package com.engine.gameutils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.engine.gameutils.IDTagDistributer.IDTag;
import com.engine.util.CyclicArrayList;
import com.engine.util.MaximumCapacityReachedException;

public final class Tester {
    public static void main(String[] args) {
	final IDTagDistributer tags = new IDTagDistributer(5000);
	
	CyclicArrayList<Integer> frTests = new CyclicArrayList<Integer>(1000000);
	
	for (int i = 0; i < frTests.getCapacity(); i++) {
	    try {
		frTests.add(i);
	    } catch (MaximumCapacityReachedException e) {
		e.printStackTrace();
	    }
	}
	
	Thread thread = new Thread() {
	    @Override 
	    public void run() {
		while (true) {
		    ArrayList<Integer> ints = new ArrayList<Integer>();
		    for (int i = 0; i < frTests.getCapacity() / 200; i++) {
			try {
			ints.add(frTests.removeNext());
		    } catch (ConcurrentModificationException e) {
			    System.out.println("CoMod");
			}
		    }
		    
		    for (int i = 0; i < ints.size(); i++) {
			try {
			    frTests.add(i);
			} catch (MaximumCapacityReachedException e) {
			    e.printStackTrace();
			} catch (ConcurrentModificationException e) {
			    System.out.println("CoMod");
			}
		    }
		}
	    }
	};
	Thread thread2 = new Thread() {
	    @Override 
	    public void run() {
		while (true) {
		    ArrayList<Integer> ints = new ArrayList<Integer>();
		    for (int i = 0; i < frTests.getCapacity() / 200; i++) {
			
			    ints.add(frTests.removeNext());
			
		    }
		    
		    for (int i = 0; i < ints.size(); i++) {
			try {
			    frTests.add(i);
			} catch (MaximumCapacityReachedException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	};
	thread.start();
	thread2.start();
	while (true) {}
    }
}
