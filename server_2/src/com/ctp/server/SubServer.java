package com.ctp.server;

import java.util.ArrayList;

import com.ctp.packets.Packet;
import com.ctp.packets.PacketHandler;
import com.ctp.users.UserConnection;
import com.ctp.util.RecentIDList;

public final class SubServer extends Thread implements Runnable {
	
	public static final int MIN_SIZE = 50;
	private volatile boolean kill = false;
	public final ArrayList<Packet> packets = new ArrayList<Packet>();
	public final Server server;
	public final int subServerIndex;
	public final int maxSize;
	public final ArrayList<UserConnection> connections = new ArrayList<UserConnection>();
	private final RecentIDList recentIDList;
	private final PacketHandler packetHandler;
	private int size = 0;
	private int recentIDListProportions;

	public SubServer(final Server server, final int subServerIndex,
			final int maxSize) {
		this.server = server;
		this.subServerIndex = subServerIndex;
		this.packetHandler = new PacketHandler(server);
		if (maxSize >= MIN_SIZE && maxSize <= Integer.MAX_VALUE) {
			this.maxSize = maxSize;
		} else {
			this.maxSize = MIN_SIZE;
		}
		recentIDList = new RecentIDList(Integer.parseInt(""
				+ (int) (.25 * this.maxSize)), Integer.parseInt(""
				+ (int) (.2 * this.maxSize)));
		recentIDListProportions = (int) .35 * this.maxSize;
		this.setPriority(MAX_PRIORITY);
		this.start();
	}

	public void addConnection(UserConnection newConnection) {
		newConnection.setUserID(getPreLoginID());
		connections.add(newConnection);
		size++;
	}

	public UserConnection getConnection(int id)
			throws InvalidIdentifierException {
		int index = -1;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e) {
			server.getServerIO().printErr(
					"[" + this + "] Exception, Invalid ID " + id + ".");
			server.getServerIO().writeException(e);
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
		server.getServerIO()
				.printErr(
						"["
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
					tries++;
					if (tries == 30) {
						System.out.println("Null Packet.. Req "+packets.get(i).getRequest() );
						packets.remove(i);
						continue;
					} else {
						i--;
						continue;
					}
				}
				System.out.println("HandlePackets");
				packets.get(i).getServersideData().serverIndex = subServerIndex;
				Packet newPacket;
				newPacket = packets.get(i);
				packetHandler.processPacket(newPacket);
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
			if (connections.get(i).getIP().equalsIgnoreCase(ip))
				return true;
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
		server.getServerIO().print(
				"[" + this + "] SubServer " + subServerIndex
						+ " up and running.");
		while (!kill) {
			getSize();
			handlePackets();
			for (int i = 0; i < size; i++) {
				if (!connections.get(i).isAlive()) {
					int id = connections.get(i).getUserID();
					server.getUserDatabase().closeProfile(id);
					connections.remove(i);
					recentIDList.updateIDHistory(id, i);
					size--;
					i--;
				}
			}
			if (server.getLowCPU()) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
					server.getServerIO().printErr(
							"[" + this + "] Error. Thread interrupted.");
					server.getServerIO().writeException(e);
				}
			}
		}
		server.getServerIO().print("[" + this + "] Killing all connections...");
		for (int i = 0; i < connections.size(); i++) {
			connections.get(i).kill();
			try {
				connections.get(i).join(30000);
			} catch (InterruptedException e) {
				server.getServerIO().printErr(
						"[" + this + "] Error. Thread interrupted.");
				server.getServerIO().writeException(e);
			}
		}
		server.getServerIO().print("[" + this + "] Killing self.");
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
		return "Server:Sub_" + subServerIndex;
	}
}
