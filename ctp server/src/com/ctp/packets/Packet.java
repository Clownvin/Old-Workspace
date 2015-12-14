package com.ctp.packets;

import java.util.ArrayList;

import com.ctp.lang.CString;
import com.ctp.util.BinaryOperations;
import com.ctp.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public class Packet {

	private Request request = Request.NULL;
	
	private Protocall protocall = Protocall.NONE;

	private ServersideData serversideData = null;
	
	private ArrayList<PacketData> data = new ArrayList<PacketData>();
	
	public Packet(Protocall protocall, Request request, ArrayList<PacketData> data) {
		this.protocall = protocall;
		this.request = request;
		this.data = data;
	}
	
	public Packet addData(PacketData data) {
		this.data.add(data);
		return this;
	}
	
	public static Packet parsePacket(byte[] bytes) {
		return fromBytes(streamUnformat(bytes));
	}
	
	public static Packet buildPacket(PacketData data) {
		Packet packet = new Packet(Protocall.NONE, Request.NULL, null).addData(data);
		return packet;
	}

	public Request getRequest() {
		return request;
	}

	public ServersideData getServersideData() {
		return serversideData;
	}

	public Packet setRequest(Request request) {
		this.request = request;
		return this;
	}
	
	public Packet setProtocall(Protocall protocall) {
		this.protocall = protocall;
		return this;
	}
	
	public PacketData getData(int index) {
		if (index >= data.size())
			throw new ArrayIndexOutOfBoundsException("Index "+index+" is greater than data length, "+data.size()+".");
		return data.get(index);
	}
	
	public int getDataAmount() {
		return data.size();
	}
	
	public DataType getDataType(int index) {
		if (index >= data.size())
			throw new ArrayIndexOutOfBoundsException("Index "+index+" is greater than data length, "+data.size()+".");
		return data.get(index).getDataType();
	}

	public Packet setServersideData(ServersideData serversideData) {
		this.serversideData = serversideData;
		return this;
	}

	public Protocall getProtocall() {
		return protocall;
	}

	public static Packet fromBytes(byte[][] bytes) {
		try {
			if (bytes[0].length < 2)
				throw new CorruptDataException("Corrupt data tried to get processed.");
		} catch (NullPointerException e) {
			System.out.println("Null byte array. (Packet.fromBytes)");
			return new Packet(Protocall.NONE, Request.NULL, new ArrayList<PacketData>()); // Blank packet
		}
		Protocall p = Protocall.getProtocal(bytes[0][0]);
		Request r = Request.getRequest(bytes[0][1]);
		ArrayList<PacketData> packetData = new ArrayList<PacketData>();
		for (int i = 1; i < bytes.length; i++) {
			packetData.add(PacketData.fromBytes(bytes[i], 0));
		}
		return new Packet(p, r, packetData);
	}

	public static  byte[][] toBytes(Packet packet) {
		byte[][] bytes = new byte[1+packet.data.size()][];
		byte[] pr = new byte[2];
		pr[0] = packet.protocall.getByteTag();
		pr[1] = packet.request.getByteTag();
		bytes[0] = pr;
		for (int i = 1; i < packet.data.size()+1; i++) {
			bytes[i] = packet.data.get(i-1).toBytes();
		}
		return bytes;
	}
	
	public static byte[] streamFormat(byte[][] bytes) {
		int totalLength = 0;
		for (byte[] b : bytes) {
			if (b != null)
				totalLength += b.length + 4;
		}
		byte[] lengthBytes = BinaryOperations.toBytes(totalLength);
		byte[] endBytes = new byte[totalLength+4];
		endBytes[0] = lengthBytes[0];
		endBytes[1] = lengthBytes[1];
		endBytes[2] = lengthBytes[2];
		endBytes[3] = lengthBytes[3];
		int index = 4;
		for (byte[] b1 : bytes) {
			if (b1 != null) {
				for (byte b3 : BinaryOperations.toBytes(b1.length)) {
					endBytes[index] = b3;
					index++;
				}
				for (byte b2 : b1) {
					endBytes[index] = b2;
					index++;
				}
			}
		}
		return endBytes;
	}
	
	public static byte[] streamFormat(byte[][] bytes, boolean incLengthBytes) {
		int totalLength = 0;
		for (byte[] b : bytes) {
			if (b != null)
				totalLength += b.length + 4;
		}
		byte[] lengthBytes = BinaryOperations.toBytes(totalLength);
		int index = incLengthBytes ? 4 : 0;
		byte[] endBytes = new byte[totalLength + index];
		if (incLengthBytes) {
			endBytes[0] = lengthBytes[0];
			endBytes[1] = lengthBytes[1];
			endBytes[2] = lengthBytes[2];
			endBytes[3] = lengthBytes[3];
		}
		for (byte[] b1 : bytes) {
			if (b1 != null) {
				for (byte b3 : BinaryOperations.toBytes(b1.length)) {
					endBytes[index] = b3;
					index++;
				}
				for (byte b2 : b1) {
					endBytes[index] = b2;
					index++;
				}
			}
		}
		return endBytes;
	}
	
	public static byte[] streamFormat(byte[][] bytes, boolean incLengthBytes, boolean checksum) {
		int totalLength = 0;
		for (byte[] b : bytes) {
			if (b != null)
				totalLength += b.length + 4;
		}
		byte[] lengthBytes = BinaryOperations.toBytes(totalLength);
		int index = (incLengthBytes ? 4 : 0) + (checksum ? 1 : 0);
		byte[] endBytes = new byte[totalLength + index];
		if (incLengthBytes) {
			endBytes[0] = lengthBytes[0];
			endBytes[1] = lengthBytes[1];
			endBytes[2] = lengthBytes[2];
			endBytes[3] = lengthBytes[3];
		}
		long cumulativeData = 0;
		for (byte[] b1 : bytes) {
			if (b1 != null) {
				for (byte b3 : BinaryOperations.toBytes(b1.length)) {
					endBytes[index] = b3;
					index++;
				}
				for (byte b2 : b1) {
					endBytes[index] = b2;
					cumulativeData += (b2 & 0xFF);
					index++;
				}
			}
		}
		if (checksum) {
			endBytes[4] = (byte) (cumulativeData % 255);
		}
		return endBytes;
	}
	
	public void printBytes() {
		System.out.println("------------------");
		byte[] bytes = streamFormat(toBytes(this), true, true);
		for (byte b : bytes)
			System.out.print((Integer.toHexString(Byte.toUnsignedInt(b)).length() < 2 ? "0"+Integer.toHexString(Byte.toUnsignedInt(b)) : Integer.toHexString(Byte.toUnsignedInt(b)))+" ");
		System.out.println();
		System.out.println((BinaryOperations.bytesToInteger(bytes) / 1000.0)+"kb of data.");
		double lastTime = System.nanoTime();
		Packet p = null;
		int loops = 100;
		for (int i = 0; i < loops; i++) {
			p = fromBytes(streamUnformat(bytes, true));
		}
		double cumulativeTime = System.nanoTime() - lastTime;
		double avg = (cumulativeTime/loops) / 1000000.0D;
		System.out.println("Data/ms: "+(BinaryOperations.bytesToInteger(bytes) / 1000.0)/avg+"kb/ms, or "+(BinaryOperations.bytesToInteger(bytes) / 1000.0)/(avg/1000)+"kb/s");
		System.out.println("Avg time per packet = "+avg+"ms");
		System.out.println("Total time for "+loops+" packet = "+(cumulativeTime) / 1000000.0D+"ms");
		System.out.println("Data amt: "+p.getDataAmount());
		System.out.println("Protocall: "+p.getProtocall());
		System.out.println("Request: "+p.getRequest());
		if (p.getDataAmount() > 0) {
			for (int i = 0; i < p.getDataAmount(); i++) {
				PacketData data = p.getData(i);
				System.out.println("Data["+i+"] is Array: "+data.isArray());
				if (data.isArray()) {
					int index = 0;
					switch (data.getDataType()) {
					case STRING:
						for (CString s : (CString[]) data.getObject()) {
							System.out.println("Data["+i+"]["+index+"]: "+s);
						}
						break;
					case INT:
						for (int j : (int[]) data.getObject()) {
							System.out.println("Data["+i+"]["+index+"]: "+j);
						}
						break;
					}
				} else {
					System.out.println("Data["+i+"] "+data.getObject());
				}
			}
		}
		System.out.println("------------------");
	}
	
	public static byte[][] streamUnformat(byte[] bytes) {
		ArrayList<byte[]> byteBlocks = new ArrayList<byte[]>();
		for (int i = 0; i < bytes.length; i += 0) {
			byte[] block = new byte[BinaryOperations.bytesToInteger(bytes, i)];
			int idx = i += 4;
			for (int j = idx, k = 0; j < block.length + idx; j++, k++) {
				try {
					block[k] = bytes[j];
					i++;
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println("Block size: "+block.length);
					System.out.println("First byte of block: "+bytes[idx]+" at index "+idx);
					System.out.println("Current packet: ");
					for (byte b : bytes)
						System.out.print((Integer.toHexString(Byte.toUnsignedInt(b)).length() < 2 ? "0"+Integer.toHexString(Byte.toUnsignedInt(b)) : Integer.toHexString(Byte.toUnsignedInt(b)))+" ");
					System.out.println();
					return null;
				}
			}
			byteBlocks.add(block);
		}
		byte[][] endByteBlocks = new byte[byteBlocks.size()][];
		int i = 0;
		for (byte[] b : byteBlocks) {
			endByteBlocks[i] = b;
			i++;
		}
		return endByteBlocks;
	}
	
	public static byte[][] streamUnformat(byte[] bytes, boolean incLengthBytes) {
		ArrayList<byte[]> byteBlocks = new ArrayList<byte[]>();
		for (int i = incLengthBytes ? 4 : 0; i < bytes.length; i += 0) {
			byte[] block = new byte[BinaryOperations.bytesToInteger(bytes, i)];
			int idx = i += 4;
			for (int j = idx, k = 0; j < block.length + idx; j++, k++) {
				try {
					block[k] = bytes[j];
					i++;
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println("Block size: "+block.length);
					System.out.println("First byte of block: "+bytes[idx]+" at index "+idx);
					System.out.println("Current packet: ");
					for (byte b : bytes)
						System.out.print((Integer.toHexString(Byte.toUnsignedInt(b)).length() < 2 ? "0"+Integer.toHexString(Byte.toUnsignedInt(b)) : Integer.toHexString(Byte.toUnsignedInt(b)))+" ");
					System.out.println();
					return null;
				}
			}
			byteBlocks.add(block);
		}
		byte[][] endByteBlocks = new byte[byteBlocks.size()][];
		int i = 0;
		for (byte[] b : byteBlocks) {
			endByteBlocks[i] = b;
			i++;
		}
		return endByteBlocks;
	}
}