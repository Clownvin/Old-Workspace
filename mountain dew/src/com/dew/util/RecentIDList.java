package com.dew.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class RecentIDList {
	private List<RecentIDListContainer> containers = new ArrayList<RecentIDListContainer>();
	@SuppressWarnings("unused")
	private int ticksTillOptimize = 20000;
	private int lastIDPointer = 0;
	private final int maxLength;
	private final int resetAmount;
	private final int resetLength;

	public RecentIDList(int maxLength, int resetAmount) {
		if (maxLength <= 0) {
			System.out
					.println("[RIDL] Exception. Max length is less than or equal to 0.");
			System.out.println("[RIDL] Max Length must be greater than 0.");
			throw new NegativeArraySizeException(
					"Max length is less than or equal to 0.");
		}
		if (resetAmount > maxLength) {
			resetAmount = maxLength;
		}
		this.maxLength = maxLength;
		this.resetAmount = resetAmount;
		this.resetLength = maxLength - resetAmount;
	}

	public void addToIDList(int lastID, int lastIndex) {
		containers.add(lastIDPointer, new RecentIDListContainer(lastID,
				lastIndex));
		lastIDPointer++;
		if (lastIDPointer == maxLength) {
			for (int i = 0; i < resetLength; i++) {
				containers.set(i, containers.get(i + resetAmount));
			}
			lastIDPointer = resetLength;
		}
	}

	public void clear() {
		containers.clear();
		lastIDPointer = 0;
	}

	public int getId(int index) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (containers.get(i).getLastIDIndex() == index) {
				return containers.get(i).getLastID();
			}
		}
		return -1;
	}

	public int getId(int index, boolean tick) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (containers.get(i).getLastIDIndex() == index) {
				if (tick)
					containers.get(i).tick();
				return containers.get(i).getLastID();
			}
		}
		return -1;
	}

	public int getIndex(int id) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (containers.get(i).getLastID() == id) {
				return containers.get(i).getLastIDIndex();
			}
		}
		return -1;
	}

	public int getIndex(int id, boolean tick) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (containers.get(i).getLastID() == id) {
				if (tick)
					containers.get(i).tick();
				return containers.get(i).getLastIDIndex();
			}
		}
		return -1;
	}

	public void reOrder() {
		Collections.sort(containers, new RecentIDListContainer(0, 0));
	}

	public int size() {
		return containers.size();
	}

	@Override
	public String toString() {
		return "Awaiting addition";
	}

	public void updateIDHistory(int idRemoved, int shiftStart) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (containers.get(i).getLastID() == idRemoved) {
				for (int j = i; j < lastIDPointer - 1; j++) {
					containers.set(j, containers.get(j + 1));
				}
				lastIDPointer--;
				return;
			}
		}
	}
}