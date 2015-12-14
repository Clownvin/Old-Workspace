package com.dew.packets;

import com.dew.packets.Packet;
import com.dew.io.ServerIO;
import com.dew.lang.CString;
import com.dew.server.SubServerManager;
import com.dew.users.UserDatabase;
import com.dew.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class PacketHandler extends AbstractPacketHandler {
	public static PacketHandler getSingleton() {
		return SINGLETON;
	}

	public void processPacket(Packet packet) {
		System.out.println("PACKET RECIEVED>>>");
		if (checkPacketListeners(packet))
			return;
		switch (packet.getRequest()) {
		case ATTEMPT_LOGIN:
			UserDatabase.loadProfile(((CString)packet.getData(0).getObject()).toString(), ((CString)packet.getData(1).getObject()).toString(), packet.getServersideData().serverIndex, packet.getServersideData().userID);
			break;
		case MESSAGE:
			System.out.println("Packet was message");
			if (SubServerManager.get(packet.getServersideData().serverIndex).getConnection(packet.getServersideData().userID).isLoggedIn()) {
				packet.getData(0).setObject(packet.getServersideData().username+": "+((CString)packet.getData(0).getObject()).toString());
				SubServerManager.sendPacketToAllLoggedIn(packet);
			} else {
				System.out.println("Not logged in.");
			}
			break;
		case ATTRIBUTE_GET:
			switch ((int)packet.getData(0).getObject()) {
			}
		default:
			System.out.println("No case for request: "+packet.getRequest());
			break;
		}
	}

	private static final PacketHandler SINGLETON = new PacketHandler();

	private PacketHandler() {
		// Prevent instantiation.
	}

	@Override
	public boolean sendPacket(Packet p) {
		return SubServerManager.get(
				p.getServersideData()
				.serverIndex)
				.getConnection(
						p.getServersideData().userID)
				.queueOutgoingPacket(p);
	}
}
