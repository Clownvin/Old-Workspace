package com.ctp.util;

public final class RecentIDList {
	private int[] lastIDs = new int[25];
	private int[] lastIDIndexes = new int[25];
	private int lastIDPointer = 0;
	private final int maxLength;
	private final int resetAmount;
	private final int resetLength;
	private boolean debug = false;

	public RecentIDList(int maxLength, int resetAmount) {
		if (maxLength <= 0) {
			System.err
					.println("[RIDL] Exception. Max length is less than or equal to 0.");
			System.err.println("[RIDL] Max Length must be greater than 0.");
			throw new NegativeArraySizeException(
					"Max length is less than or equal to 0.");
		}
		if (resetAmount > maxLength) {
			if (debug)
				System.err
						.println("[RIDL] Reset amount was greater than max length. Setting equal to max length.");
			resetAmount = maxLength;
		}
		this.maxLength = maxLength;
		this.resetAmount = resetAmount;
		this.resetLength = maxLength - resetAmount;
	}

	public void addToIDList(int lastID, int lastIndex) {
		lastIDs[lastIDPointer] = lastID;
		lastIDIndexes[lastIDPointer] = lastIndex;
		lastIDPointer++;
		if (lastIDPointer == maxLength) {
			for (int i = 0; i < resetLength; i++) {
				lastIDs[i] = lastIDs[i + resetAmount];
				lastIDIndexes[i] = lastIDIndexes[i + resetAmount];
			}
			lastIDPointer = resetLength;
		}
	}

	public void debug(boolean debug) {
		this.debug = debug;
	}

	public int getId(int index) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (lastIDIndexes[i] == index) {
				return lastIDs[i];
			}
		}
		return -1;
	}

	public int getIndex(int id) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (lastIDs[i] == id) {
				return lastIDIndexes[i];
			}
		}
		return -1;
	}

	public int size() {
		return lastIDs.length;
	}

	@Override
	public java.lang.String toString() {
		return lastIDs[0] + ", " + lastIDIndexes[0];
	}

	public void updateIDHistory(int idRemoved, int shiftStart) {
		for (int i = 0; i < lastIDPointer; i++) {
			if (lastIDs[i] == idRemoved) {
				for (int j = i; j < lastIDPointer - 1; j++) {
					lastIDs[j] = lastIDs[j + 1];
					lastIDIndexes[j] = lastIDIndexes[j + 1];
				}
				lastIDPointer--;
				for (int j = 0; j < lastIDPointer; j++) {
					if (lastIDIndexes[j] > shiftStart) {
						lastIDIndexes[j]--;
					}
				}
				return;
			}
		}
	}
}
