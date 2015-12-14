package com.dew.packets;

import com.dew.packets.Packet;
import com.dew.io.ServerIO;
import com.dew.server.SubServerManager;
import com.dew.users.UserDatabase;
import com.dew.users.UserInteraction;
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
		System.out.println("Here");
		if (checkPacketListeners(packet))
			return;
		switch (packet.getRequest()) {
		case ATTEMPT_LOGIN:
			if (packet.getDataAmount() >= 2)
				UserDatabase.loadProfile(packet.getData(0).getObject()
						.toString(), packet.getData(1).getObject().toString(),
						packet.getServersideData().serverIndex,
						packet.getServersideData().userID);
			else {
				ServerIO.printErr("[PacketHandler] Login packet only contained one or less sets of data. Should carry two.");
			}
			break;
		case LOGOUT:
			UserInteraction.notifyLogout(packet.getServersideData().userID);
			break;
		case WARNING:
			SINGLETON.sendPacket(packet.addData(new PacketData(DataType.INT, false).setObject(4)).setRequest(Request.SUCCESSFUL_LOGIN));
			break;
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
