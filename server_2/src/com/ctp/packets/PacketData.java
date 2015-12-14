package com.ctp.packets;

import java.io.Serializable;
import java.util.ArrayList;

import com.ctp.lang.CString;

public final class PacketData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2162725162338702182L;
	private DataType type = null;
	private boolean array = false;
	private Object o = null;
	
	public PacketData(DataType type, boolean array) {
		this.type = type;
		this.array = array;
	}
	
	public DataType getDataType() {
		return type;
	}
	
	public PacketData setDataType(DataType type) {
		this.type = type;
		return this;
	}
	
	public boolean isArray() {
		return array;
	}
	
	public PacketData setArray(boolean array) {
		this.array = array;
		return this;
	}
	
	public Object getObject() {
		return o;
	}
	
	public PacketData setObject(Object o) {
		this.o = o;
		return this;
	}
	
	//TEST Needs testing
	public byte[] toBytes() throws ArrayLengthException {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		byte typeByte = type.getByte();
		byte[] returnBytes;
		switch (type) {
		case STRING:
			if (array) {
				bytes.add((byte) (typeByte + (1 << 7)));
				java.lang.String[] strings = (java.lang.String[]) o;
				if (strings.length > 255) {
					throw new ArrayLengthException(strings.length+" : Array length cannot be greater than 255");
				}
				bytes.add((byte) (strings.length & 0x000000ff));
				for (java.lang.String string : strings) {
					CString s = new CString(string);
					for (byte b : s.toBytes()) {
						bytes.add(b);
					}
				}
			} else {
				bytes.add(typeByte);
				CString cString = new CString((java.lang.String) o);
				for (byte b : cString.toBytes()) {
					bytes.add(b);
				}
			}
			break;
		case INT:
			if (array) {
				bytes.add((byte) (typeByte + (1 << 7)));
				int[] ints = (int[]) o;
				if (ints.length > 255) {
					throw new ArrayLengthException(ints.length+" : Array length cannot be greater than 255");
				}
				bytes.add((byte) (ints.length & 0x000000ff));
				for (int i = 0; i < ints.length; i++) {
					bytes.add((byte) ((ints[i] >> 24) & 0xFF));
					bytes.add((byte) ((ints[i] >> 16) & 0xFF));
					bytes.add((byte) ((ints[i] >> 8) & 0xFF));
					bytes.add((byte) (ints[i] & 0xFF));
				}
			} else {
				bytes.add(typeByte);
				int i = (int) o;
				bytes.add((byte) ((i >> 24) & 0xFF));
				bytes.add((byte) ((i >> 16) & 0xFF));
				bytes.add((byte) ((i >> 8) & 0xFF));
				bytes.add((byte) (i & 0xFF));
			}
			break;
		case DOUBLE:
			long longValue;
			if (array) {
				bytes.add((byte) (typeByte + 1 << 7));
				double[] doubles = (double[]) o;
				if (doubles.length > 255) {
					throw new ArrayLengthException(doubles.length+" : Array length cannot be greater than 255");
				}
				bytes.add((byte) (doubles.length & 0x000000ff));
				for (int i = 0; i < doubles.length; i++) {
					longValue = Double.doubleToRawLongBits(doubles[i]);
					for (int j = 7; j > -1; j--)
						bytes.add((byte) ((longValue >> j * 8) & 0xFF));
				}
			} else {
				bytes.add(typeByte);
				longValue = Double.doubleToRawLongBits((double)o);
				for (int j = 7; j > -1; j--)
					bytes.add((byte) ((longValue >> j * 8) & 0xFF));
			}
			break;
		default:
			System.out.println("Unknown type: "+type);
			break;
		}
		returnBytes = new byte[bytes.size()];
		for (int i = 0; i < returnBytes.length; i++) {
			returnBytes[i] = bytes.get(i);
		}
		return returnBytes;
	}
	
	//TEST Needs testing
	public static PacketData parseData(byte[] bytes, int offset) {
		if (offset >= bytes.length) {
			System.out.println("Null packet attempted to be parsed.");
			return null;
		}
		PacketData packetData = new PacketData(DataType.getDataType((byte) (bytes[offset] & 0x7F)), (bytes[offset] & 0x80) >> 7 == 1);
		switch (packetData.getDataType()) {
		case STRING:
			ArrayList<Byte> stringBytes;
			boolean cont = true;
			int index = offset + 1;
			if (packetData.isArray()) {
				index++;
				cont = true;
				CString[] strings = new CString[bytes[offset+1]];
				for(byte b = 0; b < bytes[offset+1]; b++) {
					stringBytes = new ArrayList<Byte>();
					cont = true;
					while (cont) {
						System.out.println(bytes[index] & 0x80);
						stringBytes.add(bytes[index]);
						cont = ((byte) ((bytes[index] & 0x80) >> 7) == 1);
						index++;
					}
					strings[b] = new CString(stringBytes);
				}
				packetData.setObject(strings);
			} else {
				stringBytes = new ArrayList<Byte>();
				cont = true;
				while (cont) {
					stringBytes.add(bytes[index]);
					cont = ((bytes[index] & 0x80)>>7) == 1;
					index++;
				}
				packetData.setObject(new CString(stringBytes));
			}
			break;
		case INT:
			if (packetData.isArray()) {
				int[] ints = new int[bytes[offset+1]];
				for(byte b = 0; b < bytes[offset+1]; b++) {
					int i = bytes[offset+(b*4)+2] << 24;
					i += bytes[offset+(b*4)+3] << 16;
					i += bytes[offset+(b*4)+4] << 8;
					i += bytes[offset+(b*4)+5];
					ints[b] = i;
				}
				packetData.setObject(ints);
			} else {
				int i = bytes[offset+1] << 24;
				i += bytes[offset+2] << 16;
				i += bytes[offset+3] << 8;
				i += bytes[offset+4];
				packetData.setObject(i);
			}
			break;
		case DOUBLE: //TEST Needs a SHAT ton of testing....
			long longValue;
			if (packetData.isArray()) {
				double[] doubles = new double[bytes[offset+1]];
				for(byte b = 0; b < bytes[offset+1]; b++) {
					longValue = bytes[offset+(b*8)+2] << 56;
					byte k = 3;
					for (byte j = 6; j > -1; j--) {
						longValue += bytes[offset+(b*8)+k] << 8 * j;
						k++;
					}
					doubles[b] = Double.longBitsToDouble(longValue);
				}
				packetData.setObject(doubles);
			} else {
				longValue = bytes[offset+1] << 56;
				byte k = 2;
				for (byte j = 6; j > -1; j--) {
					longValue += bytes[offset+k] << 8 * j;
					k++;
				}
				packetData.setObject(Double.longBitsToDouble(longValue));
			}
			break;
		default:
			System.out.println("Unknown type: "+packetData.getDataType());
			break;
		}
		return packetData;
	}
}
