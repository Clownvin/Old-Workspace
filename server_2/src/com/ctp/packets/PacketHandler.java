package com.ctp.packets;

import com.ctp.server.Server;
import com.ctp.packets.DoubleDataPacket;

public final class PacketHandler {
	private final Server server;

	public PacketHandler(Server server) {
		this.server = server;
	}
	
	public void processPacket(Packet packet) {
		switch (packet.getRequest()) {
		case ATTEMPT_LOGIN:
			server.getUserDatabase().loadProfile(
					((com.ctp.lang.CString) ((DoubleDataPacket) packet).getData1().getObject()).toString(),
					((com.ctp.lang.CString) ((DoubleDataPacket) packet).getData2().getObject()).toString(),
					packet.getServersideData().serverIndex,
					packet.getServersideData().userID);
			break;
		case LOGOUT:
			server.getUserDatabase().closeProfile(packet.getServersideData().userID);
			break;
		default:
			break;
		}
	}
}
