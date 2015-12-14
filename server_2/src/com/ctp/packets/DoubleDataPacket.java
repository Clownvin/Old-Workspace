package com.ctp.packets;

public final class DoubleDataPacket extends Packet {
	private PacketData data1;
	private PacketData data2;
	
	public DoubleDataPacket (PacketData data1, PacketData data2) {
		this.data1 = data1;
		this.data2 = data2;
		this.packetDataAmount = 2;
	}
	
	public DoubleDataPacket setData1(PacketData data1) {
		this.data1 = data1;
		return this;
	}
	
	public PacketData getData1() {
		return data1;
	}
	
	public DoubleDataPacket setData2(PacketData data2) {
		this.data2 = data2;
		return this;
	}
	
	public PacketData getData2() {
		return data2;
	}
	
	public DoubleDataPacket fromBytes(byte[] bytes) {
		setRequest(Request.getRequest(bytes[0]));
		packetDataAmount = 1;
		int i = bytes[2] << 24;
		i += bytes[3] << 16;
		i += bytes[4] << 8;
		i += bytes[5];
		setData1(PacketData.parseData(bytes, 6));
		setData2(PacketData.parseData(bytes, 6+i));
		return this;
	}
	
	@Override 
	public byte[] toBytes() {
		byte[] data1Bytes = new byte[0];
		byte[] data2Bytes = new byte[0];
		try {
			data1Bytes = getData1().toBytes();
			data2Bytes = getData2().toBytes();
		} catch (ArrayLengthException e) {
			e.printStackTrace();
			return null;
		}
		byte[] returnBytes = new byte[data1Bytes.length+data2Bytes.length+10];
		int data1BytesLength = data1Bytes.length;
		returnBytes[0] = (byte) (returnBytes.length >> 24 & 0xFF);
		returnBytes[1] = (byte) (returnBytes.length >> 16 & 0xFF);
		returnBytes[2] = (byte) (returnBytes.length >> 8 & 0xFF);
		returnBytes[3] = (byte) (returnBytes.length & 0xFF);
		returnBytes[4] = getRequest().getByteID();
		returnBytes[5] = packetDataAmount;
		returnBytes[6] = (byte) (data1BytesLength >> 24 & 0xFF);
		returnBytes[7] = (byte) (data1BytesLength >> 16 & 0xFF);
		returnBytes[8] = (byte) (data1BytesLength >> 8 & 0xFF);
		returnBytes[9] = (byte) (data1BytesLength & 0xFF);
		for (int i = 0; i < data1Bytes.length; i++) {
			returnBytes[i+10] = data1Bytes[i];
		}
		for (int i = 0; i < data2Bytes.length; i++) {
			returnBytes[i+10+data1BytesLength] = data2Bytes[i];
		}
		return returnBytes;
	}

}
