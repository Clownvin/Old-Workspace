package com.ctp.users;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.ctp.server.SubServer;
import com.ctp.packets.Packet;
import com.ctp.packets.ServersideData;

public final class UserConnection extends Thread implements Runnable {
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
			while (streamAlive) {
				try {
					byte[] buffer = new byte[4];
					for (int i = 0; i < 4; i++) {
						buffer[i] = (byte) in.read();
					}
					if ((buffer[0] << 24) + (buffer[1] << 16) + (buffer[2] << 8) + (buffer[3]) < 0) {
						continue;
					}
					buffer = new byte[(buffer[0] << 24) + (buffer[1] << 16) + (buffer[2] << 8) + (buffer[3])];
					int amount = in.read(buffer);
					Packet newPacket = (Packet) Packet.buildPacket(buffer, amount);
					if (newPacket != null) {
						newPacket.setServersideData(new ServersideData());
						newPacket.getServersideData().userID = userID;
						newPacket.getServersideData().username = getUsername();
						System.out.println("Read packet: Req "+newPacket.getRequest() );
						System.out.println(newPacket != null);
						subServer.packets.add(newPacket);
					}
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
		InputStream input = null;
		OutputStream output = null;
		try {
			input = this.socket.getInputStream();
			output = this.socket.getOutputStream();
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
					if (packets.get(0) != null) {
						System.out.println("Sending packet... Req: "+packets.get(0).getRequest());
						out.write(packets.get(0).toBytes());
						out.flush();
					} else {
						System.out.println("Null Packet...");
					}
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