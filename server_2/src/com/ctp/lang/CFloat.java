package com.ctp.lang;

public final class CFloat implements CVariable {
	private Float floatValue;

	public CFloat(float floatValue) {
		this.floatValue = floatValue;
	}

	public CFloat add(float f) {
		floatValue += f;
		return this;
	}

	public CFloat subtract(float f) {
		floatValue -= f;
		return this;
	}

	public CFloat multiply(float f) {
		floatValue *= f;
		return this;
	}

	public CFloat divide(float f) {
		floatValue /= f;
		return this;
	}

	public CFloat set(float f) {
		floatValue = f;
		return this;
	}

	public float get() {
		return floatValue;
	}

	@Override
	public byte[] toBytes() {
		int intValue = Float.floatToRawIntBits(floatValue);
		return new byte[] { FLOAT_CODE, (byte) ((intValue >> 24) & 0xFF),
				(byte) ((intValue >> 16) & 0xFF),
				(byte) ((intValue >> 8) & 0xFF), (byte) (intValue & 0xFF) };
	}

	public CFloat fromBytes(byte[] bytes) {
		return new CFloat(Float.intBitsToFloat((bytes[0] << 24) + (bytes[1] << 16)
				+ (bytes[2] << 8) + bytes[3]));
	}

	public CFloat fromBytes(byte[] bytes, int start) {
		return new CFloat(Float.intBitsToFloat((bytes[start] << 24)
				+ (bytes[start + 1] << 16) + (bytes[start + 2] << 8)
				+ bytes[start + 3]));
	}

	public CFloat[] arrayFromBytes(byte[] bytes) {
		CFloat[] toReturn = new CFloat[bytes.length / 4];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CFloat(Float.intBitsToFloat((bytes[(i * 4)] << 24)
					+ (bytes[(i * 4) + 1] << 16) + (bytes[(i * 4) + 2] << 8)
					+ bytes[(i * 4) + 3]));
		}
		return toReturn;
	}

	public CFloat[] arrayFromBytes(byte[] bytes, int start) {
		CFloat[] toReturn = new CFloat[(bytes.length - start) / 4];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = new CFloat(Float.intBitsToFloat((bytes[(i * 4) + start] << 24)
					+ (bytes[(i * 4) + 1 + start] << 16)
					+ (bytes[(i * 4) + 2 + start] << 8)
					+ bytes[(i * 4) + 3 + start]));
		}
		return toReturn;
	}

	@Override
	public String toString() {
		return floatValue + "";
	}
}
