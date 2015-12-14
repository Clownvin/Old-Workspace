package com.cserver.lang;

public final class CDouble implements CVariable {
	private Double doubleValue;
	private static final CDouble INSTANCE = new CDouble(0);

	public CDouble(double doubleValue) {
		this.doubleValue = doubleValue;
	}
	
	public static CDouble getInstance() {
		return INSTANCE;
	}

	public CDouble add(double d) {
		doubleValue += d;
		return this;
	}

	public CDouble subtract(double d) {
		doubleValue -= d;
		return this;
	}

	public CDouble multiply(double d) {
		doubleValue *= d;
		return this;
	}

	public CDouble divide(double d) {
		doubleValue /= d;
		return this;
	}

	public CDouble set(double d) {
		doubleValue = d;
		return this;
	}

	public double get() {
		return doubleValue;
	}

	@Override
	public byte[] toBytes() {
		long longValue = Double.doubleToRawLongBits(doubleValue);
		return new byte[] { DOUBLE_CODE, (byte) ((longValue >> 56) & 0xFF),
				(byte) ((longValue >> 48) & 0xFF),
				(byte) ((longValue >> 40) & 0xFF),
				(byte) ((longValue >> 32) & 0xFF),
				(byte) ((longValue >> 24) & 0xFF),
				(byte) ((longValue >> 16) & 0xFF),
				(byte) ((longValue >> 8) & 0xFF), (byte) (longValue & 0xFF) };
	}
	
	@Override
	public CDouble fromBytes(byte[] bytes) {
		return new CDouble(Double.longBitsToDouble((long) (bytes[0] << 56)
				+ (long) (bytes[1] << 48) + (long) (bytes[2] << 40)
				+ (long) (bytes[3] << 32) + (long) (bytes[4] << 24)
				+ (long) (bytes[5] << 16) + (long) (bytes[6] << 8)
				+ (long) (bytes[7])));
	}
	
	@Override
	public CDouble fromBytes(byte[] bytes, int start) {
		return new CDouble(Double.longBitsToDouble((long) (bytes[start] << 56)
				+ (long) (bytes[start + 1] << 48)
				+ (long) (bytes[start + 2] << 40)
				+ (long) (bytes[start + 3] << 32)
				+ (long) (bytes[start + 4] << 24)
				+ (long) (bytes[start + 5] << 16)
				+ (long) (bytes[start + 6] << 8) + (long) (bytes[start + 7])));
	}
	
	@Override
	public CDouble[] arrayFromBytes(byte[] bytes) {
		CDouble[] toReturn = new CDouble[bytes.length / 8];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CDouble(Double.longBitsToDouble((long) (bytes[i * 8] << 56)
					+ (long) (bytes[(i * 8) + 1] << 48)
					+ (long) (bytes[(i * 8) + 2] << 40)
					+ (long) (bytes[(i * 8) + 3] << 32)
					+ (long) (bytes[(i * 8) + 4] << 24)
					+ (long) (bytes[(i * 8) + 5] << 16)
					+ (long) (bytes[(i * 8) + 6] << 8)
					+ (long) (bytes[(i * 8) + 7])));
		}
		return toReturn;
	}
	
	@Override
	public CDouble[] arrayFromBytes(byte[] bytes, int start) {
		CDouble[] toReturn = new CDouble[(bytes.length - start) / 8];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CDouble(Double
					.longBitsToDouble((long) (bytes[(i * 8) + start] << 56)
							+ (long) (bytes[(i * 8) + 1 + start] << 48)
							+ (long) (bytes[(i * 8) + 2 + start] << 40)
							+ (long) (bytes[(i * 8) + 3 + start] << 32)
							+ (long) (bytes[(i * 8) + 4 + start] << 24)
							+ (long) (bytes[(i * 8) + 5 + start] << 16)
							+ (long) (bytes[(i * 8) + 6 + start] << 8)
							+ (long) (bytes[(i * 8) + 7 + start])));
		}
		return toReturn;
	}

	@Override
	public String toString() {
		return doubleValue + "";
	}
}
