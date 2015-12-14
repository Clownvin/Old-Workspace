package com.cserver.connection.packets;

public final class Packet {
	private Protocall protocall;
	private Request request;
	private PacketData[] packetData;
	
	public Packet(Protocall protocall, Request request, int amountOfData) {
		this.protocall = protocall;
		this.request = request;
		packetData = new PacketData[amountOfData];
	}
}
