package com.hexane.server;

import java.util.ArrayList;

import com.hexane.packets.Packet;
import com.hexane.users.UserConnection;

public final class SubServerManager {
	private final Server server;
	private final ArrayList<SubServer> subServers = new ArrayList<SubServer>();
	private double percentToCreateSub = 1.0D;
	private int maxSubs = 10;
	private int subServerMaxSize = 100;

	public SubServerManager(final Server server) {
		this.server = server;
		server.getServerIO().print(
				"[" + this + "] SubServerManager is up and running.");
	}

	public void add(SubServer server) {
		subServers.add(server);
	}

	public boolean allSubServersFull() {
		for (int i = 0; i < size(); i++) {
			if ((subServers.get(i).getSize() < (getPercentToCreateSub() * subServerMaxSize) && subServers
					.size() < getMaxSubs())
					|| (subServers.get(i).getSize() < subServerMaxSize && subServers
							.size() == getMaxSubs())) {
				return false;
			}
		}
		return true;
	}

	public SubServer get(int index) {
		return subServers.get(index);
	}

	public UserConnection getConnection(int id)
			throws InvalidIdentifierException {
		for (SubServer server : subServers) {
			if (server.hasConnection(id))
				return server.getConnection(id);
		}
		throw new InvalidIdentifierException("[" + this + "] Exception, " + id
				+ " is not a valid ID.");
	}

	public SubServer getLowestPopulatedServer() {
		int lowest = Integer.MAX_VALUE;
		int lowestIndex = 0;
		for (int i = 0; i < subServers.size(); i++) {
			if (subServers.get(i).getSize() < lowest) {
				lowest = subServers.get(i).getSize();
				lowestIndex = i;
			}
		}
		return subServers.get(lowestIndex);
	}

	public int getMaxSubs() {
		return maxSubs;
	}

	public double getPercentToCreateSub() {
		return percentToCreateSub;
	}

	public int getSubServerMaxSize() {
		return subServerMaxSize;
	}

	public boolean ipLoggedIn(String ip) {
		for (int i = 0; i < subServers.size(); i++) {
			if (subServers.get(i).ipLoggedIn(ip))
				return true;
		}
		return false;
	}

	public void kill() {
		for (SubServer subServer : subServers) {
			subServer.kill();
			try {
				subServer.join(30000);
			} catch (InterruptedException e) {
				server.getServerIO()
						.printErr(
								"["
										+ this
										+ "] InterruptedException occurred while killing all SubServers.");
				server.getServerIO().writeException(e);
			}
		}
	}

	public void sendMessage(Packet packet) {
		for (SubServer server : subServers) {
			server.sendMessage(packet);
		}
	}

	public void setMaxSubs(int maxSubs) {
		this.maxSubs = maxSubs;
	}

	public void setPercentToCreateSub(double percentToCreateSub) {
		this.percentToCreateSub = percentToCreateSub;
	}

	public void setSubServerMaxSize(int subServerMaxSize) {
		this.subServerMaxSize = subServerMaxSize;
	}

	public int size() {
		return subServers.size();
	}

	public boolean subServerEmpty() {
		for (int i = 0; i < size(); i++) {
			if (get(i).getSize() == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "SubServerManager";
	}
}
