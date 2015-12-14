package com.ctp.packets;

import com.ctp.users.UserDatabase;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class PacketHandler {
	public static PacketHandler getSingleton() {
		return SINGLETON;
	}

	public static void processPacket(Packet packet) {
		System.out.println("Here");
		switch (packet.getRequest()) {
		case ATTEMPT_LOGIN:
			UserDatabase.loadProfile(
					packet.getData(0).getObject().toString(),
					packet.getData(1).getObject().toString(), packet
							.getServersideData().serverIndex, packet
							.getServersideData().userID);
			break;
		case LOGOUT:
			UserDatabase.closeProfile(packet.getServersideData().userID);
			break;
		default:
			break;
		}
	}

	private static final PacketHandler SINGLETON = new PacketHandler();

	private PacketHandler() {
		// Prevent instantiation.
	}
}
