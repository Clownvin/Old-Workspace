package com.ctp.users;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.ctp.server.SubServer;
import com.ctp.util.CycleProcess;
import com.ctp.util.CycleProcessManager;
import com.ctp.util.ThreadPool;
import com.ctp.io.ServerIO;
import com.ctp.packets.Packet;
import com.ctp.packets.ServersideData;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class UserConnection implements CycleProcess {
	private final SubServer subServer;
	private final Socket socket;
	private final InputStream in;
	private final OutputStream out;
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
			while (streamAlive && !killed()) {
				try {
					byte[] buffer = new byte[4];
					for (int i = 0; i < 4; i++) {
						buffer[i] = (byte) in.read();
					}
					if ((buffer[0] << 24) + (buffer[1] << 16)
							+ (buffer[2] << 8) + (buffer[3]) <= 0) {
						continue;
					}
					buffer = new byte[(buffer[0] << 24) + (buffer[1] << 16)
							+ (buffer[2] << 8) + (buffer[3])];
					in.read(buffer);
					Packet newPacket = Packet.parsePacket(buffer);
					if (newPacket != null) {
						newPacket.printBytes();
						newPacket.setServersideData(new ServersideData());
						newPacket.getServersideData().userID = userID;
						newPacket.getServersideData().username = getUsername();
						subServer.packets.add(newPacket);
					}
				} catch (SocketException e) {
					ServerIO.printErr("[" + this + "] Socket exception.");
					ServerIO.writeException(e);
					streamAlive = false;
				} catch (IOException e) {
					ServerIO.writeException(e);
				}
			}
			kill = true;
		}

		@Override
		public String toString() {
			return getUsername() + "@" + ip + "'s packetGrabber";
		}

	});

	public UserConnection(final Socket socket, final SubServer subServer) {
		CycleProcessManager.addProcess(this);
		this.socket = socket;
		this.ip = socket.getInetAddress().getHostAddress();
		this.subServer = subServer;
		InputStream input = null;
		OutputStream output = null;
		try {
			input = this.socket.getInputStream();
			output = this.socket.getOutputStream();
		} catch (IOException e) {
			ServerIO.printErr("[" + this
					+ "] Error creating input or output streams, or both.");
			ServerIO.writeException(e);
		}
		this.in = input;
		this.out = output;
		userID = subServer.getLoginQueueID();
		ThreadPool.start(packetGrabber);
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
	
	public void showStreamStuff() {
		System.out.println(socket.isBound());
		System.out.println(socket.isClosed());
		System.out.println(socket.isConnected());
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

	@Override
	public void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void process() {
		packetGrabber.interrupt();
		while (packets.size() > 0 && !killed()) {
			try {
				if (packets.get(0) != null) {
					System.out.println("Sending packet... Req: "
							+ packets.get(0).getRequest());
					out.write(Packet.streamFormat(Packet.toBytes(packets.get(0))));
					out.flush();
				} else {
					System.out.println("Null Packet...");
				}
				packets.remove(0);
			} catch (IOException e) {
				ServerIO.printErr("[" + getUsername() + "@" + ip
						+ "] Error. Output exception.");
				ServerIO.writeException(e);
				kill = true;
				packets.clear();
			}
		}
		if (socket.isClosed()) {
			kill();
			packetGrabber.interrupt();
		}
	}

	public boolean killed() {
		return kill;
	}

	@Override
	public boolean endConditionMet() {
		return killed();
	}
}