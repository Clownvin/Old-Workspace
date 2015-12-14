package com.cserver.lang;

public final class CByte implements CVariable {
	private byte byteValue;
	private static final CByte INSTANCE = new CByte((byte) 0);

	public CByte(byte byteValue) {
		this.byteValue = byteValue;
	}
	
	public static CByte getInstance() {
		return INSTANCE;
	}

	public CByte add(byte b) {
		byteValue += b;
		return this;
	}

	public CByte subtract(byte b) {
		byteValue -= b;
		return this;
	}

	public CByte multiply(byte b) {
		byteValue *= b;
		return this;
	}

	public CByte divide(byte b) {
		byteValue /= b;
		return this;
	}

	public CByte set(byte b) {
		byteValue = b;
		return this;
	}

	public byte get() {
		return byteValue;
	}

	@Override
	public byte[] toBytes() {
		return new byte[] { BYTE_CODE, byteValue };
	}
	
	@Override
	public CByte fromBytes(byte[] bytes) {
		return new CByte(bytes[0]);
	}
	
	@Override
	public CByte fromBytes(byte[] bytes, int start) {
		return new CByte(bytes[start]);
	}
	
	@Override
	public CByte[] arrayFromBytes(byte[] bytes) {
		CByte[] toReturn  = new CByte[bytes.length];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CByte(bytes[i]);
		}
		return toReturn;
	}
	
	@Override
	public CByte[] arrayFromBytes(byte[] bytes, int start) {
		CByte[] toReturn  = new CByte[bytes.length - 1];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CByte(bytes[i+start]);
		}
		return toReturn;
	}
	
	@Override
	public String toString() {
		return byteValue + "";
	}
}
