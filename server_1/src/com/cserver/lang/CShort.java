package com.cserver.lang;

public final class CShort implements CVariable {
	private short shortValue;

	public CShort(short shortValue) {
		this.shortValue = shortValue;
	}

	public CShort add(short s) {
		shortValue += s;
		return this;
	}

	public CShort subtract(short s) {
		shortValue -= s;
		return this;
	}

	public CShort multiply(short s) {
		shortValue *= s;
		return this;
	}

	public CShort divide(short s) {
		shortValue /= s;
		return this;
	}

	public CShort set(short s) {
		shortValue = s;
		return this;
	}

	public short get() {
		return shortValue;
	}

	@Override
	public byte[] toBytes() {
		return new byte[] { SHORT_CODE, (byte) ((shortValue >> 8) & 0xFF),
				(byte) (shortValue & 0xFF) };
	}

	@Override
	public String toString() {
		return shortValue + "";
	}

	public static CShort fromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
