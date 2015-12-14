package com.cserver.lang;

public final class CInteger implements CVariable {
	private int integerValue;

	public CInteger(int integerValue) {
		this.integerValue = integerValue;
	}

	public CInteger add(int f) {
		integerValue += f;
		return this;
	}

	public CInteger subtract(int f) {
		integerValue -= f;
		return this;
	}

	public CInteger multiply(int f) {
		integerValue *= f;
		return this;
	}

	public CInteger divide(int f) {
		integerValue /= f;
		return this;
	}

	public CInteger set(int f) {
		integerValue = f;
		return this;
	}

	public float get() {
		return integerValue;
	}

	@Override
	public byte[] toBytes() {
		return new byte[] { INTEGER_CODE, (byte) ((integerValue >> 24) & 0xFF),
				(byte) ((integerValue >> 16) & 0xFF),
				(byte) ((integerValue >> 8) & 0xFF),
				(byte) (integerValue & 0xFF) };
	}

	public static CInteger fromBytes(byte[] bytes) {
		return (bytes[0] << 24) + (bytes[1] << 16) + (bytes[2] << 8) + bytes[3];
	}

	public static int fromBytes(byte[] bytes, int start) {
		return (bytes[start] << 24) + (bytes[start + 1] << 16)
				+ (bytes[start + 2] << 8) + bytes[start + 3];
	}

	public static int[] arrayFromBytes(byte[] bytes) {
		int[] toReturn = new int[bytes.length / 4];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = (bytes[(i * 4)] << 24) + (bytes[(i * 4) + 1] << 16)
					+ (bytes[(i * 4) + 2] << 8) + bytes[(i * 4) + 3];
		}
		return toReturn;
	}

	public static int[] arrayFromBytes(byte[] bytes, int start) {
		int[] toReturn = new int[(bytes.length - start) / 4];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = (bytes[(i * 4) + start] << 24)
					+ (bytes[(i * 4) + 1 + start] << 16)
					+ (bytes[(i * 4) + 2 + start] << 8)
					+ bytes[(i * 4) + 3 + start];
		}
		return toReturn;
	}

	@Override
	public String toString() {
		return integerValue + "";
	}
}