package com.ctp.util;

import java.util.Comparator;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public class RecentIDListContainer implements Comparator<RecentIDListContainer> {
	private int lastID = -1;
	private int lastIDIndex = -1;
	private int timesGotten = 0;

	public RecentIDListContainer(int lastID, int lastIDIndex) {
		this.lastID = lastID;
		this.lastIDIndex = lastIDIndex;
	}

	@Override
	public int compare(RecentIDListContainer arg0, RecentIDListContainer arg1) {
		return arg0.getTimesGotten() - arg1.getTimesGotten();
	}

	public int getLastID() {
		return lastID;
	}

	public int getLastIDIndex() {
		return lastIDIndex;
	}

	public int getTimesGotten() {
		return timesGotten;
	}

	public void setLastID(int lastID) {
		this.lastID = lastID;
	}

	public void setLastIDIndex(int lastIDIndex) {
		this.lastIDIndex = lastIDIndex;
	}

	public void setTimesGotten(int timesGotten) {
		this.timesGotten = timesGotten;
	}

	public void tick() {
		timesGotten++;
	}
}
