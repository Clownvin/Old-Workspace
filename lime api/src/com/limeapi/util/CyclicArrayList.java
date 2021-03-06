package com.limeapi.util;

import java.util.Arrays;
import java.util.ConcurrentModificationException;

public class CyclicArrayList<T> {

    private transient volatile Object[] arrayData;

    private int takePointer = 0;
    private int addPointer = 0;
    private int size = 0;
    private volatile long modCount = 0L;

    private static final Object[] EMPTY_ARRAYDATA = new Object[0];

    public CyclicArrayList(int initialCapacity) {
	if (initialCapacity > 0) {
	    this.arrayData = new Object[initialCapacity];
	} else if (initialCapacity == 0) {
	    this.arrayData = EMPTY_ARRAYDATA;
	} else {
	    throw new IllegalArgumentException("Illegal Capacity: "
		    + initialCapacity);
	}
    }

    public void add(T t) {
	modCount++;
	addPointer %= arrayData.length;
	takePointer %= arrayData.length;
	if (addPointer == takePointer && size > 0) {
	    throw new MaximumCapacityReachedException(
		    "Cannot overlap already written data.");
	}
	checkForComodification();
	size++;
	arrayData[addPointer++] = t;
    }

    // Checks for concurrent modification.
    // This somehow works.
    private void checkForComodification() {
	if (CyclicArrayList.this.modCount != this.modCount)
	    throw new ConcurrentModificationException();
    }

    public void clear() {
	modCount++;
	for (int i = 0; i < arrayData.length; i++) {
	    arrayData[i] = null;
	}
	size = 0;
    }

    @SuppressWarnings("unchecked")
    // TEST Needs testing. Rough sketch.
    public T get(int index) {
	modCount++;
	if (index > size)
	    throw new ArrayIndexOutOfBoundsException(
		    "ArrayIndexOutOfBoundsException: " + index
			    + ", max possible: " + size);
	try {
	    checkForComodification();
	    return (T) arrayData[(takePointer + index) % arrayData.length];
	} catch (ClassCastException e) {
	    System.err
		    .println("ClassCastException in RotationalArray.get(int index)");
	    throw e;
	}
    }
    
    public void set(int index, T value) {
	modCount++;
	if (index > size)
	    throw new ArrayIndexOutOfBoundsException(
		    "ArrayIndexOutOfBoundsException: " + index
			    + ", max possible: " + size);
	checkForComodification();
	arrayData[(takePointer + index) % arrayData.length] = value;
    }
    
    @SuppressWarnings("unchecked") // Should not matter unless someone's been fiddlin with it.
    public T[] asArray(T[] containerArray) {
	if (containerArray.length != size) {
	    throw new IllegalArgumentException("Container array must be the same length as the amount of data in the CyclicArrayList.");
	}
	int tempPointer = takePointer;
	for (int i = 0; i < size; i++) {
	    tempPointer %= arrayData.length;
	    containerArray[tempPointer] = (T) arrayData[tempPointer++]; 
	}
	return containerArray;
    }

    public int getCapacity() {
	return arrayData.length;
    }

    public boolean hasAvailable() {
	for (Object o : arrayData) {
	    if (o != null) {
		return true;
	    }
	}
	return false;
    }

    // Suppressing because you shouldn't be able to put an invalid type into
    // array.
    @SuppressWarnings("unchecked")
    public T removeNext() {
	modCount++;
	takePointer %= arrayData.length;
	size--;
	if (arrayData[takePointer] == null) {
	    if (!hasAvailable()) {
		if (takePointer != addPointer) {
		    System.err
			    .println("Probably has been overtaken, run tests...");
		}
		return null;
	    }
	}
	T toReturn = null;
	try {
	    toReturn = (T) arrayData[takePointer];
	} catch (ClassCastException e) {
	    System.err
		    .println("ClassCastException in RotationalArray.removeNext()");
	    throw e;
	}
	checkForComodification();
	arrayData[takePointer++] = null;
	return toReturn;
    }

    /**
     * Warning: May cause loss of capacity;
     * 
     * @param newCapacity
     */
    // We're just going to keep things simple and clear it each time this is
    // called.
    public synchronized void setCapacity(int newCapacity) {
	checkForComodification();
	if (newCapacity > 0) {
	    takePointer = 0;
	    addPointer = 0;
	    this.arrayData = Arrays.copyOf(arrayData, newCapacity);
	} else {
	    throw new IllegalArgumentException("Illegal Capacity: "
		    + newCapacity);
	}
    }

    public int size() {
	return size;
    }
}
