package com.cserver.connection.packets;

import com.cserver.lang.CByte;
import com.cserver.lang.CCharacter;
import com.cserver.lang.CDouble;
import com.cserver.lang.CFloat;
import com.cserver.lang.CInteger;
import com.cserver.lang.CLong;
import com.cserver.lang.CShort;
import com.cserver.lang.CString;
import com.cserver.lang.CVariable;

public final class PacketData {
	private DataType dataType = DataType.UNKNOWN;
	private boolean array = false;
	private CVariable object;

	public PacketData(DataType dataType) {
		this.dataType = dataType;
	}

	public PacketData setArray(boolean array) {
		this.array = array;
		return this;
	}

	public boolean getArray() {
		return array;
	}

	public PacketData setDataType(DataType dataType) {
		this.dataType = dataType;
		return this;
	}

	public DataType getDataType() {
		return dataType;
	}

	public CVariable getObject() {
		return object;
	}

	public PacketData setObject(CVariable object) {
		this.object = object;
		return this;
	}

	public byte[] toBytes() {
		byte[] objectBytes = object.toBytes();
		byte[] returnBytes = new byte[objectBytes.length + (array ? 1 : 0)];
		for (int i = 0; i < objectBytes.length; i++) {
			returnBytes[i] = objectBytes[i];
		}
		returnBytes[0] += (byte) (array ? (byte) 0 : (byte) 1) << 7;
		if (array) {
			returnBytes[returnBytes.length - 1] = (byte) 0xFF;
		}
		return returnBytes;
	}

	@SuppressWarnings("rawtypes")
	public static PacketData fromBytes(byte[] bytes) {
		DataType type = DataType.fromByte((byte) (bytes[0] & 0x7F));
		boolean array = (bytes[0] & 0x80) >> 7 == 1;
		if (!array)
			switch (type) {
			case STRING:
				return new PacketData(type).setObject(CString
						.fromBytes(bytes));
			case CHAR:
				return new PacketData(type).setObject(new CCharacter('a').fromBytes(bytes));
			case BYTE:
				return new PacketData(type).setObject(CByte.getInstance().fromBytes(bytes));
			case SHORT:
				return new PacketData(type).setObject(CShort.fromBytes(bytes));
			case INT:
				return new PacketData(type).setObject(CInteger.fromBytes(bytes));
			case FLOAT:
				return new PacketData(type).setObject(CFloat.fromBytes(bytes));
			case LONG:
				return new PacketData<CLong>(type).setObject(CLong.fromBytes(bytes));
			case DOUBLE:
				return new PacketData<CDouble>(type).setObject(CDouble.fromBytes(bytes));
			default:
				return new PacketData<CVariable>(type);
			}
		else
			switch (type) {
			case STRING:
				return new PacketData<CString>(type).setObject(CString
						.arrayFromBytes(bytes));
			case CHAR:
				return new PacketData<CCharacter[]>(type).setObject(CCharacter.arrayFromBytes(bytes));
			case BYTE:
				return new PacketData<CByte[]>(type).setObject(CByte.arrayFromBytes(bytes));
			case SHORT:
				return new PacketData<CShort[]>(type).setObject(CShort.arrayFromBytes(bytes));
			case INT:
				return new PacketData<CInteger[]>(type).setObject(CInteger.arrayFromBytes(bytes));
			case FLOAT:
				return new PacketData<CFloat>(type).setObject(CFloat.arrayFromBytes(bytes));
			case LONG:
				return new PacketData<CLong>(type).setObject(CLong.arrayFromBytes(bytes));
			case DOUBLE:
				return new PacketData<CDouble>(type).setObject(CDouble.arrayFromBytes(bytes));
			default:
				return new PacketData<CVariable>(type);
			}
	}
}
