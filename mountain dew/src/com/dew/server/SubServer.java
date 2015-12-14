package com.dew.server;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import com.dew.connection.Connection;
import com.dew.io.ServerIO;
import com.dew.lang.InvalidIdentifierException;
import com.dew.packets.Packet;
import com.dew.packets.PacketComparator;
import com.dew.packets.PacketHandler;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.users.UserDatabase;
import com.dew.util.RecentIDList;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class SubServer extends Thread implements Runnable {

	public static final int MIN_SIZE = 50;
	private volatile boolean kill = false;
	private final ArrayList<Packet> packets = new ArrayList<Packet>();
	public final int subServerIndex;
	public final int maxSize;
	public final ArrayList<Connection> connections = new ArrayList<Connection>();
	private final RecentIDList recentIDList;
	private int size = 0;
	private int recentIDListProportions;

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
		this.start();
	}

	public void addConnection(Connection newConnection) {
		newConnection.setUserID(getPreLoginID());
		connections.add(newConnection);
		size++;
	}

	// TODO Consider Revision
	public Connection getConnection(int id) throws InvalidIdentifierException {
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

	// TODO Consider Revision
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
			try {
				packets.sort(new PacketComparator());
			} catch (ConcurrentModificationException e) {
				ServerIO.printDebug("["
						+ this
						+ "] Concurrent modification exception. (SubServer.handlePackets)");
			}
			for (int i = 0; i < packets.size(); i++) {
				if (packets.get(i) == null) {
					try {
						sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					tries++;
					if (tries == 2) {
						tries = 0; // Too lazy to see if I really need this.
						packets.remove(i);
					}
					i--;
					continue;
				} else if (packets.get(i).getRequest() == Request.NULL
						|| packets.get(i).getProtocall() == Protocall.NONE) {
					packets.remove(i);
					i--;
					continue;
				}
				packets.get(i).getServersideData().serverIndex = subServerIndex;
				PacketHandler.getSingleton().processPacket(packets.get(i));
				packets.remove(i);
				i--;
				tries = 0;
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
				return true;
			}
		}
		return false;
	}

	public void kill() {
		kill = true;
		synchronized (this) {
			this.notifyAll();
		}
	}

	public void killConnection(int id) throws InvalidIdentifierException {
		getConnection(id).kill();
		try {
			recentIDList.updateIDHistory(id, getIndex(id));
		} catch (InvalidIdentifierException e) {
			throw e;
		}
	}

	public boolean queuePacket(Packet p) {
		if (packets.size() < connections.size() * 2) { // 2 Packets per connection.
			packets.add(p);
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		ServerIO.print("[" + this + "] SubServer " + subServerIndex
				+ " up and running.");
		while (!kill) {
			handlePackets();
			for (int i = 0; i < size; i++) {
				if (connections.get(i).killed()) {
					int id = connections.get(i).getUserID();
					if (UserDatabase.closeProfile(id)) {
						ServerIO.printDebug("[" + this
								+ "] Closed user profile successfully.");
					} else {
						ServerIO.printErr("[" + this
								+ "] Failed to close user's profile.");
					}
					ServerIO.printDebug("[" + this + "] Removing connection: "
							+ connections.get(i));
					connections.remove(i);
					recentIDList.updateIDHistory(id, i);
					size--;
					i--;
				}
			}
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				ServerIO.print("SubServer wait interrupted...");
				e.printStackTrace();
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
				connections.get(i).queueOutgoingPacket(packet);
			}
		}
	}

	@Override
	public String toString() {
		return "SubServer " + subServerIndex;
	}
}
