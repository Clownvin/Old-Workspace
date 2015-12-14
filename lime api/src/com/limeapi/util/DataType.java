package com.limeapi.util;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public enum DataType {
    UNKNOWN((byte) -1), STRING((byte) 0), INT((byte) 1), DOUBLE((byte) 2), BOOLEAN(
	    (byte) 3), LONG((byte) 4), FLOAT((byte) 5), VOID((byte) 6), BYTE(
	    (byte) 7);
    public static DataType getDataType(byte b) {
	switch (b) {
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
	case 5:
	    return FLOAT;
	case 6:
	    return VOID;
	case 7:
	    return BYTE;
	case -1:
	default:
	    return UNKNOWN;
	}
    }

    private final byte typeByte;

    private DataType(byte typeByte) {
	this.typeByte = typeByte;
    }

    public byte getByte() {
	return typeByte;
    }
}
