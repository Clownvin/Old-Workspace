package com.hexane.packets;

import com.hexane.server.InvalidIdentifierException;
import com.hexane.server.Server;

public final class PacketHandler {
	private final Server server;

	public PacketHandler(Server server) {
		this.server = server;
	}
	
	public void processPacket(Packet packet) {

		switch (packet.request) {
		case ATTEMPT_LOGIN:
			server.getUserDatabase().loadProfile(
					(String) ((DoubleDataPacket) packet).data1.o,
					(String) ((DoubleDataPacket) packet).data2.o,
					packet.serversideData.serverIndex,
					packet.serversideData.userID);
			break;
		case LOGOUT:
			server.getUserDatabase().closeProfile(packet.serversideData.userID);
			break;
		case CLICK:
			// TODO Add
			break;
		case CLICK_OBJECT:
			break;
		case CHAT_MESSAGE:
			SingleDataPacket packet2 = new SingleDataPacket();
			packet2.request = Request.CHAT_MESSAGE;
			int clip = ((String) ((SingleDataPacket) packet).data.o).length();
			if (clip > 120) {
				clip = 120;
			}
			try {
				if (((String) ((SingleDataPacket) packet).data.o).replaceAll(
						" ", "").length() > 0)
					packet2.data.o = server.getSubServerManager()
							.get(packet.serversideData.serverIndex)
							.getConnection(packet.serversideData.userID)
							.getUsername()
							+ ": "
							+ ((String) ((SingleDataPacket) packet).data.o)
									.substring(0, clip);
				else
					return;
			} catch (InvalidIdentifierException e) {
				server.getServerIO().writeException(e);
			}
			server.getSubServerManager().sendMessage(packet2);
			break;
		default:
			break;
		}
	}
}
