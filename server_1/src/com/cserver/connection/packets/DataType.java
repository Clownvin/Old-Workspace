package com.cserver.connection.packets;

import com.cserver.lang.CVariable;

public enum DataType {
	UNKNOWN("Unknown"), STRING("String"), CHAR("Char"), SHORT("Short"), BYTE(
			"Byte"), INT("Int"), LONG("Long"), DOUBLE("Double"), FLOAT("Float"), BOOLEAN(
			"Boolean");
	private final String string;

	private DataType(final String string) {
		this.string = string;
	}
	
	public static DataType fromByte(byte code) {
		switch (code) {
		case CVariable.BYTE_CODE:
			return BYTE;
		case CVariable.SHORT_CODE:
			return SHORT;
		case CVariable.INTEGER_CODE:
			return INT;
		case CVariable.FLOAT_CODE:
			return FLOAT;
		case CVariable.LONG_CODE:
			return LONG;
		case CVariable.DOUBLE_CODE:
			return DOUBLE;
		case CVariable.BOOLEAN_CODE:
			return BOOLEAN;
		case CVariable.STRING_CODE:
			return STRING;
		case CVariable.CHARACTER_CODE:
			return CHAR;
		default:
			System.out.println("Unknown: "+code);
			return UNKNOWN;
		}
	}

	public static byte toByte(DataType dataType) {
		switch (dataType) {
		case UNKNOWN:
			return -1;
		case STRING:
			return CVariable.STRING_CODE;
		case CHAR:
			return CVariable.CHARACTER_CODE;
		case SHORT:
			return CVariable.SHORT_CODE;
		case BYTE:
			return CVariable.BYTE_CODE;
		case INT:
			return CVariable.INTEGER_CODE;
		case LONG:
			return CVariable.LONG_CODE;
		case DOUBLE:
			return CVariable.DOUBLE_CODE;
		case FLOAT:
			return CVariable.FLOAT_CODE;
		case BOOLEAN:
			return CVariable.BOOLEAN_CODE;
		default:
			System.out.println("Unknown: " + dataType);
			return -1;
		}
	}

	@Override
	public String toString() {
		return string;
	}
}
