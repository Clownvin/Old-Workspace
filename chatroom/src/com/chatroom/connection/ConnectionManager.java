package com.chatroom.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import com.chatroom.packets.ChatRoomPacketHandler;
import com.dew.io.ServerIO;
import com.dew.packets.Packet;
import com.dew.packets.PacketData;
import com.dew.packets.PacketHandler;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.util.BinaryOperations;
import com.dew.util.CyclicArrayList;
import com.dew.util.DataType;
import com.dew.util.Utilities;

// Send/Recieve packets and open the socket
public final class ConnectionManager extends Thread {
    private static final int WAITINGPACKET_LIMIT = 4;
    private static final Packet OVERFLOW_WARNING_PACKET = Packet.buildPacket(
	    Protocall.URGENT, Request.WARNING, new PacketData(DataType.INT,
		    false).setObject(1)); // Warning packet data = warning code
    private final CyclicArrayList<Packet> waitingPackets = new CyclicArrayList<Packet>(
	    WAITINGPACKET_LIMIT);
    private final ArrayList<Packet> outgoingPackets = new ArrayList<Packet>();
    private volatile boolean kill = false;
    private InputStream in = null;
    private OutputStream out = null;
    private Socket socket = null;
    private static final Object NOTIFICATION_OBJECT = new Object();

    public ConnectionManager(String ip, int port) {
	int attempts = 0;
	while (attempts < 2000) {
	    try {
		socket = new Socket(ip, port);
		break;
	    } catch (UnknownHostException e) {
		attempts++;
	    } catch (IOException e) {
		attempts++;
	    }
	}
	if (attempts == 2000) {
	    JOptionPane.showMessageDialog(null,
		    "Could not connect to the server.", "Connection Error",
		    JOptionPane.WARNING_MESSAGE);
	    System.exit(1);
	}
	try {
	    in = socket.getInputStream();
	    out = socket.getOutputStream();
	} catch (IOException e) {
	    System.exit(2);
	}
	new PacketGrabber().start();
	this.start();
    }

    public boolean killed() {
	return kill;
    }

    public void kill() {
	kill = true;
    }

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
			    synchronized (waitingPackets) {
				waitingPackets.add(newPacket);
				System.out.println("Got packet...");
				synchronized (NOTIFICATION_OBJECT) {
				    NOTIFICATION_OBJECT.notifyAll();
				}
			    }
			} else {
			    sendUrgentPacket(OVERFLOW_WARNING_PACKET);
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
	    return "PacketGrabber";
	}
    }

    public boolean sendUrgentPacket(Packet packet) {
	try {
	    out.write(Utilities.streamFormat(Packet.toBytes(packet
		    .setProtocall(Protocall.URGENT))));
	    out.flush();
	    return true;
	} catch (IOException e) {
	    ServerIO.printErr("Error occured while sending urgent packet.");
	}
	return false;
    }

    public boolean sendPacket(Packet packet) {
	System.out.println("queueing outgoing packet.");
	outgoingPackets.add(packet);
	synchronized (NOTIFICATION_OBJECT) {
	    NOTIFICATION_OBJECT.notifyAll();
	}
	return true;
    }

    @Override
    public void run() {
	while (!killed()) {
	    while (outgoingPackets.size() > 0 && !killed()) {
		try {
		    if (outgoingPackets.get(0) != null) {
			out.write(Utilities.streamFormat(Packet
				.toBytes(outgoingPackets.get(0))));
			out.flush();
		    }
		    outgoingPackets.remove(0);
		} catch (IOException e) {
		    kill = true;
		    outgoingPackets.clear();
		}
	    }
	    if (waitingPackets.size() > 0) {
		synchronized (waitingPackets) {
		    ChatRoomPacketHandler.getSingleton().processPacket(
			    waitingPackets.removeNext());
		}
	    }
	    synchronized (NOTIFICATION_OBJECT) {
		try {
		    NOTIFICATION_OBJECT.wait(1);
		} catch (InterruptedException e) {
		}
	    }
	}
    }
}
