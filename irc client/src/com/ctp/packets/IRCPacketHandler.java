package com.ctp.packets;

import java.io.IOException;

import com.ctp.irc.IRCClient;
import com.dew.chaos.ChaosIRC;
import com.dew.chaos.ChaosGUI;
import com.dew.gui.GUI;
import com.dew.packets.AbstractPacketHandler;
import com.dew.packets.Packet;
import com.dew.util.BinaryOperations;
import com.dew.util.Utilities;

public class IRCPacketHandler extends AbstractPacketHandler {
	public final IRCClient client;

	public IRCPacketHandler(final IRCClient client) {
		this.client = client;
	}

	public void handlePacket(Packet packet) {
		if (packet == null || packet.getRequest() == null) {
			System.out.println("Null");
			return;
		}
	}
	
	public void sendPacket(Packet p) {
		System.out.println("Sending packet");
		try {
			((IRCClient) client).getOutputStream().write(Utilities.streamFormat(Packet.toBytes(p)));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error sending packet.");
		}
	}
}