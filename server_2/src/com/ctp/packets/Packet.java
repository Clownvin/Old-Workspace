package com.ctp.packets;

public abstract class Packet{
	private Request request = Request.NULL;
	protected byte packetDataAmount = 0;
	private ServersideData serversideData = null;
	
	public byte[] toBytes() {
		return null;
	}
	
	public static Packet parsePacket(byte[] bytes) {
		return null;
	}

	public ServersideData getServersideData() {
		return serversideData;
	}

	public Packet setServersideData(ServersideData serversideData) {
		this.serversideData = serversideData;
		return this;
	}

	public Request getRequest() {
		return request;
	}

	public Packet setRequest(Request request) {
		this.request = request;
		return this;
	}
	
	public static Packet buildPacket(byte[] bytes, int totalRead) {
		if (bytes.length < 2)
			return null;
		byte[] realBytes = new byte[totalRead];
		for (int i = 0; i < totalRead; i++) {
			realBytes[i] = bytes[i];
		}
		switch (realBytes[1]) {
		case 1:
			return new SingleDataPacket(null).fromBytes(realBytes);
		case 2:
			return new DoubleDataPacket(null, null).fromBytes(realBytes);
		}
		return null;
	}

}