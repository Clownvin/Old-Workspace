package com.dew.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import com.dew.io.ServerIO;
import com.dew.packets.Packet;
import com.dew.packets.PacketData;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.packets.ServersideData;
import com.dew.server.SubServer;
import com.dew.util.BinaryOperations;
import com.dew.util.CycleProcess;
import com.dew.util.CycleProcessManager;
import com.dew.util.DataType;
import com.dew.util.Utilities;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class Connection implements CycleProcess {
	private final class PacketGrabber extends Thread implements Runnable {

		@Override
		public void run() {
			int averagePacketSize = 40;
			boolean streamAlive = true;
			int times = 0;
			while (streamAlive && !killed()) {
				try {
					byte[] buffer = new byte[4];
					for (int i = 0; i < 4; i++) {
						buffer[i] = (byte) in.read();
						if (buffer[i] == -1) {
							if (times == 8)
								kill();
							else
								times++;
							continue;
						}
						if (times > 0) {
							times = 0;
						}
					}
					int length = BinaryOperations.bytesToInteger(buffer);
					if (length <= 0) {
						continue;
					}
					buffer = new byte[length];
					in.read(buffer);
					Packet newPacket = Packet.parsePacket(buffer);
					if (newPacket != null) {
						averagePacketSize = (averagePacketSize + length) / 2;
						if (waitingPackets.size() < WAITINGPACKET_LIMIT) {
							newPacket.setServersideData(new ServersideData());
							newPacket.getServersideData().userID = userID;
							newPacket.getServersideData().username = getUsername();
							synchronized (waitingPackets) {
								waitingPackets.add(newPacket);
							}
						} else {
							sendUrgentPacket(OVERFLOW_WARNING_PACKET);
							if (in.available() > averagePacketSize * 5) {  // They haven't heeded warning, so cutting connection.
								in.close();
								out.close();
								streamAlive = false;
							}
						}
					}
				} catch (SocketException e) {
					ServerIO.printErr("[" + this + "] Socket exception.");
					ServerIO.writeException(e);
					streamAlive = false;
				} catch (IOException e) {
					ServerIO.writeException(e);
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {
					ServerIO.printErr("["
							+ this
							+ "] Unexpected interrupt. Line 107 : UserConnection.PacketGrabber.run()");
				}
			}
			kill = true;
		}

		@Override
		public String toString() {
			return getUsername() + "@" + ip + "'s packetGrabber";
		}
	}
	private static final int WAITINGPACKET_LIMIT = 4;
	//TODO Sort packets by urgency
	private static final Packet OVERFLOW_WARNING_PACKET = Packet.buildPacket(
			Protocall.URGENT, Request.WARNING, new PacketData(DataType.INT,
					false).setObject(1)); // Warning packet data = warning code
	private final SubServer subServer;
	private final Socket socket;
	private final InputStream in;
	private final OutputStream out;
	private final String ip;
	private final ArrayList<Packet> outgoingPackets = new ArrayList<Packet>();
	private final ArrayList<Packet> waitingPackets = new ArrayList<Packet>();
	private volatile boolean kill = false;
	private boolean loggedIn = false;
	private int userID = -1;
	private String username = "~user";

	private final PacketGrabber packetGrabber = new PacketGrabber();

	public Connection(final Socket socket, final SubServer subServer) {
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
		packetGrabber.start();
	}

	@Override
	public void end() {
		synchronized (subServer) {
			subServer.notifyAll();
		}
	}

	@Override
	public boolean endConditionMet() {
		return killed();
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

	public boolean killed() {
		return kill;
	}

	public void logIn(int id, String username) {
		this.userID = id;
		this.setLoggedIn(true);
		this.setUsername(username);
	}

	@Override
	public void process() {
		synchronized (outgoingPackets) {
		while (outgoingPackets.size() > 0 && !killed()) {
			try {
				if (outgoingPackets.get(0) != null) {
					synchronized (out) {
					out.write(Utilities.streamFormat(Packet
							.toBytes(outgoingPackets.get(0))));
					out.flush();
					}
				}
				outgoingPackets.remove(0);
			} catch (IOException e) {
				ServerIO.printErr("[" + getUsername() + "@" + ip
						+ "] Error. Output exception.");
				ServerIO.writeException(e);
				kill = true; // A little melodramatic, aren't we? ;)
				outgoingPackets.clear();
			}
		}
		}
		if (waitingPackets.size() > 0) {
			synchronized (waitingPackets) {
				if (subServer.queuePacket(waitingPackets.get(0)))
					waitingPackets.remove(0);
			}
			synchronized (subServer) {
				subServer.notifyAll();
			}
		}
	}

	//Boolean because maybe in the future I might set limitations or something.
	public synchronized boolean queueOutgoingPacket(Packet packet) {
		synchronized (outgoingPackets) {
			outgoingPackets.add(packet);
		}
		return true;
	}

	public boolean sendUrgentPacket(Packet packet) {
		synchronized (out) {
		try {
			out.write(Utilities.streamFormat(Packet.toBytes(packet
					.setProtocall(Protocall.URGENT))));
			out.flush();
			return true;
		} catch (IOException e) {
			ServerIO.printErr("Error occured while sending urgent packet.");
		}
		}
		return false;
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