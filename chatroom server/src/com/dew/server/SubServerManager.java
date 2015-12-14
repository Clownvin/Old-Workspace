package com.dew.server;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.dew.connection.Connection;
import com.dew.io.ServerIO;
import com.dew.lang.InvalidIdentifierException;
import com.dew.packets.Packet;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class SubServerManager {
	public static void add(SubServer server) {
		SUB_SERVERS.add(server);
	}

	public static boolean allSubServersFull() {
		for (int i = 0; i < size(); i++) {
			if ((SUB_SERVERS.get(i).getSize() < (getPercentToCreateSub() * subServerMaxSize) && SUB_SERVERS
					.size() < getMaxSubs())
					|| (SUB_SERVERS.get(i).getSize() < subServerMaxSize && SUB_SERVERS
							.size() == getMaxSubs())) {
				return false;
			}
		}
		return true;
	}

	public static SubServer get(int index) {
		return SUB_SERVERS.get(index);
	}

	public static Connection getConnection(int id) {
		for (SubServer server : SUB_SERVERS) {
			if (server.hasConnection(id))
				return server.getConnection(id);
		}
		throw new InvalidIdentifierException("[SubServerManager] Exception, "
				+ id + " is not a valid UserConnection ID.");
	}

	public static SubServer getLowestPopulatedServer() {
		int lowest = Integer.MAX_VALUE;
		int lowestIndex = 0;
		for (int i = 0; i < SUB_SERVERS.size(); i++) {
			if (SUB_SERVERS.get(i).getSize() < lowest) {
				lowest = SUB_SERVERS.get(i).getSize();
				lowestIndex = i;
			}
		}
		return SUB_SERVERS.get(lowestIndex);
	}

	public static int getMaxSubs() {
		return maxSubs;
	}

	public static double getPercentToCreateSub() {
		return percentToCreateSub;
	}

	public static SubServerManager getSingleton() {
		return SINGLETON;
	}

	public static int getSubServerMaxSize() {
		return subServerMaxSize;
	}

	public static boolean ipLoggedIn(String ip) {
		for (int i = 0; i < SUB_SERVERS.size(); i++) {
			if (SUB_SERVERS.get(i).ipLoggedIn(ip))
				return true;
		}
		return false;
	}

	public static void kill() {
		for (SubServer subServer : SUB_SERVERS) {
			subServer.kill();
			try {
				subServer.join(30000);
			} catch (InterruptedException e) {
				ServerIO.printErr("[SubServerManager] InterruptedException occurred while killing all SubServers.");
				ServerIO.writeException(e);
			}
		}
	}

	public static void listSubServers() {
		ServerIO.print("[SubServerManager] Currently active SubServers: "
				+ SUB_SERVERS.size());
		ServerIO.print("-----------------------------------------------------------");
		DecimalFormat df = new DecimalFormat("#");
		df.setMaximumFractionDigits(8);
		for (SubServer subServer : SUB_SERVERS) {
			ServerIO.print(subServer + "; Population: " + subServer.getSize()
					+ ", POC: " + (subServer.getSize() / getSubServerMaxSize())
					* 100 + "%");
		}
		ServerIO.print("-----------------------------------------------------------");
	}
	
	public static void sendPacketToAll(Packet packet) {
		for (SubServer server : SUB_SERVERS) {
			server.sendPacketToAll(packet);
		}
	}
	
	public static void sendPacketToAllLoggedIn(Packet packet) {
		for (SubServer server : SUB_SERVERS) {
			server.sendPacketToAllLoggedIn(packet);
		}
	}

	public static void setMaxSubs(int _maxSubs) {
		maxSubs = _maxSubs;
	}

	public static void setPercentToCreateSub(double _percentToCreateSub) {
		percentToCreateSub = _percentToCreateSub;
	}

	public static void setSubServerMaxSize(int _subServerMaxSize) {
		subServerMaxSize = _subServerMaxSize;
	}

	public static int size() {
		return SUB_SERVERS.size();
	}

	public static boolean subServerEmpty() {
		for (int i = 0; i < size(); i++) {
			if (get(i).getSize() == 0) {
				return true;
			}
		}
		return false;
	}

	private static final ArrayList<SubServer> SUB_SERVERS = new ArrayList<SubServer>();

	private static double percentToCreateSub = 1.0D;

	private static int maxSubs = 10;

	private static int subServerMaxSize = 100;

	private static final SubServerManager SINGLETON = new SubServerManager();

	static {
		ServerIO.print("[SubServerManager] SubServerManager is up and running.");
	}

	private SubServerManager() {
		// To prevent instantiation.
	}
}
