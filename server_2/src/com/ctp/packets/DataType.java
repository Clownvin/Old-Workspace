package com.ctp.packets;

public enum DataType {
	UNKNOWN((byte) -1), STRING((byte) 0), INT((byte) 1), DOUBLE((byte) 2), BOOLEAN((byte) 3), LONG((byte) 4);
	private final byte typeByte;
	
	private DataType(byte typeByte) {
		this.typeByte = typeByte;
	}
	
	public byte getByte() {
		return typeByte;
	}
	
	public static DataType getDataType(byte b) {
		switch(b) {
		case 0:
			return STRING;
		case 1:
			return INT;
		case 2:
			return DOUBLE;
		case 3:
			return BOOLEAN;
		case 4:
			return LONG;
		case -1:
		default:
			return UNKNOWN;
		}
	}
}
