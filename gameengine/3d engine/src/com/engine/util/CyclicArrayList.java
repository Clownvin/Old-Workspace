package com.engine.util;

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

	/**
	 * Warning: Invoking this causes all currently stored data to be erased.
	 * 
	 * @param newCapacity
	 */
	// We're just going to keep things simple and clear it each time this is
	// called.
	public void setCapacity(int newCapacity) {
		modCount++;
		if (newCapacity > 0) {
			checkForComodification();
			takePointer = 0;
			addPointer = 0;
			clear();
			this.arrayData = Arrays.copyOf(arrayData, newCapacity);
		} else {
			throw new IllegalArgumentException("Illegal Capacity: "
					+ newCapacity);
		}
	}

	private void checkForComodification() {
		if (CyclicArrayList.this.modCount != this.modCount)
			throw new ConcurrentModificationException();
	}

	@SuppressWarnings("unchecked")
	public T getNext() throws NoEntriesException {
		modCount++;
		takePointer %= arrayData.length;
		size--;
		if (arrayData[takePointer] == null) {
			if (!hasAvailable()) {
				if (takePointer != addPointer) {
					System.err
							.println("Probably has been overtaken, run tests...");
				}
				throw new NoEntriesException("CyclicArrayList");
			}
		}
		try {
			checkForComodification();
			return (T) arrayData[takePointer++];
		} catch (ClassCastException e) {
			System.err
					.println("ClassCastException in RotationalArray.getNext()");
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
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

	public boolean hasAvailable() {
		for (Object o : arrayData) {
			if (o != null) {
				return true;
			}
		}
		return false;
	}

	public int getCapacity() {
		return arrayData.length;
	}

	public void add(T t) throws MaximumCapacityReachedException {
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

	public int size() {
		return size;
	}

	public void set(int index, T t) {
		modCount++;
		if (index < 0 || index > size)
			throw new IndexOutOfBoundsException("IndexOutOfBoundsException: "
					+ index);
		arrayData[index] = t;
	}

	public void clear() {
		modCount++;
		for (int i = 0; i < arrayData.length; i++) {
			arrayData[i] = null;
		}
		addPointer = 0;
		takePointer = 0;
		size = 0;
	}
}