package com.cserver.lang;

public interface CVariable {
	public static final byte BYTE_CODE = 0;
	public static final byte SHORT_CODE = 1;
	public static final byte INTEGER_CODE = 2;
	public static final byte FLOAT_CODE = 3;
	public static final byte LONG_CODE = 4;
	public static final byte DOUBLE_CODE = 5;
	public static final byte CHARACTER_CODE = 6;
	public static final byte STRING_CODE = 7;
	public static final byte BOOLEAN_CODE = 8;

	public byte[] toBytes();
	
	public CVariable fromBytes(byte[] bytes);
	public CVariable fromBytes(byte[] bytes, int start);
	public CVariable[] arrayFromBytes(byte[] bytes);
	public CVariable[] arrayFromBytes(byte[] bytes, int start);
}
