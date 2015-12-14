package com.ctp.server;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ctp.io.ServerIO;
import com.ctp.packets.Packet;
import com.ctp.packets.PacketHandler;
import com.ctp.packets.Protocall;
import com.ctp.packets.Request;
import com.ctp.users.UserConnection;
import com.ctp.users.UserDatabase;
import com.ctp.util.RecentIDList;
import com.ctp.util.ThreadPool;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class SubServer extends Thread implements Runnable {

	public static final int MIN_SIZE = 50;
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");
	private static long startOfBlockTime = System.nanoTime();
	private volatile boolean kill = false;
	public final ArrayList<Packet> packets = new ArrayList<Packet>();
	public final int subServerIndex;
	public final int maxSize;
	public final ArrayList<UserConnection> connections = new ArrayList<UserConnection>();
	private final RecentIDList recentIDList;
	private int size = 0;
	private int recentIDListProportions;
	private long cycles = 0;
	private long cumulativeTime = 0;
	private long lastTime = 0;

	public SubServer(final int subServerIndex, final int maxSize) {
		this.subServerIndex = subServerIndex;
		if (maxSize >= MIN_SIZE && maxSize <= Integer.MAX_VALUE) {
			this.maxSize = maxSize;
		} else {
			this.maxSize = MIN_SIZE;
		}
		recentIDList = new RecentIDList((int) (.25 * this.maxSize),
				(int) (.2 * this.maxSize));
		recentIDListProportions = (int) .35 * this.maxSize;
		this.setPriority(MAX_PRIORITY);
		ThreadPool.start(this);
	}

	public void addConnection(UserConnection newConnection) {
		newConnection.setUserID(getPreLoginID());
		connections.add(newConnection);
		size++;
	}
	
	public double getAverageTimeMilliseconds() {
		if (cycles == 0)
			cycles = 1;
		double avg = (cumulativeTime/cycles) / 1000000.0D;
		return avg;
	}
	
	public double getAverageTimeMilliseconds(boolean clear) {
		if (cycles == 0)
			cycles = 1;
		double avg = (cumulativeTime/cycles) / 1000000.0D;
		if (clear) {
			cycles = 0;
			cumulativeTime = 0;
		}
		return avg;
	}

	public UserConnection getConnection(int id)
			throws InvalidIdentifierException {
		int index = -1;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e) {
			ServerIO.printErr("[" + this + "] Exception, Invalid ID " + id
					+ ".");
			ServerIO.writeException(e);
		}
		if (index > -1) {
			return connections.get(index);
		}
		throw new InvalidIdentifierException("[" + this + "] ID " + id
				+ " is an Invalid Identifier. RIDL length = " + recentIDList
				+ ", " + connections.get(0).getUserID());
	}

	public int getIndex(int id) throws InvalidIdentifierException {
		int index = -1;
		if (size > recentIDListProportions) {
			index = recentIDList.getIndex(id);
		}
		if (index > -1) {
			return index;
		}
		if (index < 0)
			for (int i = 0; i < size; i++) {
				if (connections.get(i).getUserID() == id) {
					recentIDList.addToIDList(id, i);
					return i;
				}
			}
		throw new InvalidIdentifierException("[" + this
				+ "] Index containing data on ID " + id + " does not exist.");
	}

	public int getLoginQueueID() {
		for (int i = -1; i > Integer.MIN_VALUE; i--) {
			boolean usable = true;
			for (int j = 0; j < connections.size(); j++) {
				if (connections.get(j).getUserID() == i) {
					usable = false;
				}
			}
			if (usable) {
				return i;
			}
		}
		ServerIO.printErr("["
				+ this
				+ "] Max login queue IDs. I don't know how that's even possible.");
		return -1;
	}

	public int getPreLoginID() {
		boolean usable;
		for (int i = -1; i > Integer.MIN_VALUE; i--) {
			usable = true;
			for (int j = 0; j < size; j++) {
				if (connections.get(j).getUserID() == j) {
					usable = false;
					continue;
				}
			}
			if (usable) {
				return i;
			}
		}
		return Integer.MIN_VALUE;
	}

	public int getSize() {
		return size;
	}

	public void handlePackets() {
		int tries = 0;
		synchronized (packets) {
			for (int i = 0; i < packets.size(); i++) {
				if (packets.get(i) == null) {
					try {
						sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tries++; // DEFFO find a way to incorporate threading and the object wait/notify and stuff... OOORR, find out if needed, since it's now a CycleProcess
					if (tries == 30) {
						System.out.println("Null Packet.. Req "
								+ packets.get(i).getRequest());
						packets.remove(i);
					}
					i--;
					continue;
				} else if (packets.get(i).getRequest() == Request.NULL || packets.get(i).getProtocall() == Protocall.NONE) {
					packets.remove(i);
					i--;
					continue;
				}
				packets.get(i).getServersideData().serverIndex = subServerIndex;
				Packet newPacket;
				newPacket = packets.get(i);
				PacketHandler.processPacket(newPacket);
				packets.remove(i);
				i--;
			}
		}
	}

	public boolean hasConnection(int id) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getUserID() == id)
				return true;
		}
		return false;
	}

	public boolean ipLoggedIn(String ip) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getIP().equalsIgnoreCase(ip)) {
				connections.get(i).showStreamStuff();
				return true;
			}
		}
		return false;
	}

	public void kill() {
		kill = true;
	}

	public void killConnection(int id) throws InvalidIdentifierException {
		getConnection(id).kill();
		try {
			recentIDList.updateIDHistory(id, getIndex(id));
		} catch (InvalidIdentifierException e) {
			throw e;
		}
	}

	@Override
	public void run() {
		DECIMAL_FORMAT.setMaximumFractionDigits(8);
		ServerIO.print("[" + this + "] SubServer " + subServerIndex
				+ " up and running.");
		while (!kill) {
			getSize();
			handlePackets();
			for (int i = 0; i < size; i++) {
				if (connections.get(i).killed()) {
					int id = connections.get(i).getUserID();
					UserDatabase.closeProfile(id);
					ServerIO.printDebug("[" + this + "] Removing connection: "
							+ connections.get(i));
					connections.remove(i);
					recentIDList.updateIDHistory(id, i);
					size--;
					i--;
				}
			}
			if (lastTime != 0) {
				cumulativeTime += System.nanoTime() - lastTime;
				cycles++;
			}
			/*if ((cumulativeTime / 60000000000.0D) >= 1)
				ServerIO.print("["+this+"] Average cycle time over 1 minute: "+DECIMAL_FORMAT.format(getAverageTimeMilliseconds(true))+"ms.");*/
			if ((System.nanoTime() - startOfBlockTime) / 60000000000.0D >= 5) {
				startOfBlockTime = System.nanoTime();
				ServerIO.print("["+this+"] Average cycle time over 5 minutes: "+DECIMAL_FORMAT.format(getAverageTimeMilliseconds(true))+"ms.");
			}
			lastTime = System.nanoTime();
			if (Server.getLowCPU()) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
					ServerIO.printErr("[" + this
							+ "] Error. Thread interrupted.");
					ServerIO.writeException(e);
				}
			}
		}
		ServerIO.printDebug("[" + this + "] Killing all connections...");
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).kill();
		}
		ServerIO.printDebug("[" + this + "] Killing self.");
	}

	public void sendMessage(Packet packet) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).isLoggedIn()) {
				connections.get(i).addOutgoingPacket(packet);
			}
		}
	}

	@Override
	public String toString() {
		return "SubServer " + subServerIndex;
	}
}
