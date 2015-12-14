package com.ctp.packets;

import java.util.ArrayList;

import com.dew.lang.CString;
import com.dew.lang.CorruptDataException;
import com.dew.packets.Packet;
import com.dew.packets.PacketData;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.packets.ServersideData;
import com.dew.util.BinaryOperations;
import com.dew.util.DataType;
import com.dew.util.Utilities;

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
		if (data != null)
			this.data = data;
	}
	
	public Packet addData(PacketData data) {
		this.data.add(data);
		return this;
	}
	
	public static Packet parsePacket(byte[] bytes) {
		return fromBytes(Utilities.streamUnformat(bytes));
	}
	
	public static Packet parsePacket(byte[][] bytes) {
		return fromBytes(bytes);
	}
	
	public static Packet buildPacket(Protocall p, PacketData data) {
		Packet packet = new Packet(p, Request.NULL, null).addData(data);
		return packet;
	}
	
	public static Packet buildPacket(Request r, PacketData data) {
		Packet packet = new Packet(Protocall.NONE, r, null).addData(data);
		return packet;
	}
	
	public static Packet buildPacket(Protocall p, Request r, PacketData data) {
		Packet packet = new Packet(p, r, null).addData(data);
		return packet;
	}
	
	public static Packet buildPacket(Protocall p) {
		Packet packet = new Packet(p, Request.NULL, null);
		return packet;
	}
	
	public static Packet buildPacket(Request r) {
		Packet packet = new Packet(Protocall.NONE, r, null);
		return packet;
	}
	
	public static Packet buildPacket(Protocall p, Request r) {
		Packet packet = new Packet(p, r, null);
		return packet;
	}
	
	public static Packet buildPacket(PacketData data) {
		Packet packet = new Packet(Protocall.NONE, Request.NULL, new ArrayList<PacketData>()).addData(data);
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
		if (bytes[0].length < 2)
			throw new CorruptDataException("Corrupt data tried to get processed.");
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
	
	//Entirely for testing purposes.
	public void printBytes() {
		System.out.println("------------------");
		byte[] bytes = Utilities.streamFormat(toBytes(this));
		System.out.println("Checksum: "+Integer.toHexString(Byte.toUnsignedInt(bytes[4])));
		for (byte b : bytes)
			System.out.print((Integer.toHexString(Byte.toUnsignedInt(b)).length() < 2 ? "0"+Integer.toHexString(Byte.toUnsignedInt(b)) : Integer.toHexString(Byte.toUnsignedInt(b)))+" ");
		System.out.println();
		System.out.println((BinaryOperations.bytesToInteger(bytes) / 1000.0)+"kb of data.");
		double lastTime = System.nanoTime();
		Packet p = null;
		int loops = 100;
		for (int i = 0; i < loops; i++) {
			p = fromBytes(Utilities.streamUnformat(bytes, true));
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
}