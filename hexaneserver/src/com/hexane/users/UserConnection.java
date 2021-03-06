package com.hexane.users;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.hexane.packets.Packet;
import com.hexane.packets.ServersideData;
import com.hexane.server.SubServer;

public final class UserConnection extends Thread implements Runnable {
	private final SubServer subServer;
	private final Socket socket;
	private final ObjectInputStream in;
	private final ObjectOutputStream out;
	private final String ip;
	private final ArrayList<Packet> packets = new ArrayList<Packet>();
	private volatile boolean kill = false;
	private boolean loggedIn = false;
	private int userID = -1;
	private String username = "~user";
	private Thread packetGrabber = new Thread(new Runnable() {

		@Override
		public void run() {
			boolean streamAlive = true;
			while (streamAlive) {
				try {
					Packet newPacket = (Packet) in.readObject();
					newPacket.serversideData = new ServersideData();
					newPacket.serversideData.userID = userID;
					newPacket.serversideData.username = getUsername();
					subServer.packets.add(newPacket);
				} catch (ClassNotFoundException e) {
					subServer.server.getServerIO().printErr(
							"[" + this
									+ "] Exception, Cannot find class Packet.");
					subServer.server.getServerIO().writeException(e);
					streamAlive = false;
				} catch (SocketException e) {
					subServer.server.getServerIO().printErr(
							"[" + this + "] Socket exception.");
					subServer.server.getServerIO().writeException(e);
					streamAlive = false;
				} catch (IOException e) {
					subServer.server.getServerIO().writeException(e);
					streamAlive = false;
				}
			}
			kill = true;
		}

	});

	public UserConnection(final Socket socket, final SubServer subServer) {
		this.socket = socket;
		this.ip = socket.getInetAddress().getHostAddress();
		this.subServer = subServer;
		ObjectInputStream input = null;
		ObjectOutputStream output = null;
		try {
			input = new ObjectInputStream(this.socket.getInputStream());
			output = new ObjectOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {
			subServer.server
					.getServerIO()
					.printErr(
							"["
									+ this
									+ "] Error creating input or output streams, or both.");
			subServer.server.getServerIO().writeException(e);
		}
		this.in = input;
		this.out = output;
		this.setPriority(NORM_PRIORITY);
		userID = subServer.getLoginQueueID();
		this.start();
	}

	public void addOutgoingPacket(Packet packet) {
		packets.add(packet);
	}

	public String getIP() {
		return ip;
	}

	public int getUserID() {
		return userID;
	}

	public String getUsername() {
		return username;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void kill() {
		kill = true;
	}

	public void logIn(int id, String username) {
		this.userID = id;
		this.setLoggedIn(true);
		this.setUsername(username);
	}

	@Override
	public void run() {
		packetGrabber.start();
		while (!kill) {
			while (packets.size() > 0 && !kill) {
				try {
					out.writeObject(packets.get(0));
					out.flush();
					packets.remove(0);
				} catch (IOException e) {
					subServer.server.getServerIO().printErr(
							"[" + this + "] Error. Output exception.");
					subServer.server.getServerIO().writeException(e);
					kill = true;
					packets.clear();
				}
			}
			if (subServer.server.getLowCPU() && !kill) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
					subServer.server.getServerIO().printErr(
							"[" + this + "] Error. Thread interrupted.");
					subServer.server.getServerIO().writeException(e);
				}
			}
		}
		try {
			packetGrabber.join(3000);
			in.close();
			out.close();
			socket.close();
		} catch (IOException | InterruptedException e) {
			subServer.server
					.getServerIO()
					.printErr(
							"["
									+ this
									+ "] Error. Failed closing either IN, OUT, SOCKET, or PacketGrabber.");
			subServer.server.getServerIO().writeException(e);
		}
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public void setUserID(final int userID) {
		this.userID = userID;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return getUsername() + "@" + ip;
	}
}