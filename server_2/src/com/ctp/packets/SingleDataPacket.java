package com.ctp.packets;

public final class SingleDataPacket extends Packet {

	private PacketData data;
	
	public SingleDataPacket(PacketData data) {
		this.setData(data);
		packetDataAmount = 1;
	}
	
	@Override 
	public byte[] toBytes() {
		byte[] dataBytes = new byte[0];
		try {
			dataBytes = getData().toBytes();
		} catch (ArrayLengthException e) {
			e.printStackTrace();
			return null;
		}
		byte[] returnBytes = new byte[dataBytes.length+6];
		returnBytes[0] = (byte) ((returnBytes.length >> 24) & 0xFF);
		returnBytes[1] = (byte) ((returnBytes.length >> 16) & 0xFF);
		returnBytes[2] = (byte) ((returnBytes.length >> 8) & 0xFF);
		returnBytes[3] = (byte) (returnBytes.length & 0xFF);
		returnBytes[4] = getRequest().getByteID();
		returnBytes[5] = packetDataAmount;
		for (int i = 0; i < dataBytes.length; i++) {
			returnBytes[i+6] = dataBytes[i];
		}
		return returnBytes;
	}
	
	public SingleDataPacket fromBytes(byte[] bytes) {
		setRequest(Request.getRequest(bytes[0]));
		packetDataAmount = 1;
		setData(PacketData.parseData(bytes, (byte) 2));
		return this;
	}

	public PacketData getData() {
		return data;
	}

	public SingleDataPacket setData(PacketData data) {
		this.data = data;
		return this;
	}
}
