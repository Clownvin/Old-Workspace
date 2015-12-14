package com.ctp.lang;

public final class CLong implements CVariable {
	private long longValue;

	public CLong(long longValue) {
		this.longValue = longValue;
	}

	public CLong add(long l) {
		longValue += l;
		return this;
	}

	public CLong subtract(long l) {
		longValue -= l;
		return this;
	}

	public CLong multiply(long l) {
		longValue *= l;
		return this;
	}

	public CLong divide(long l) {
		longValue /= l;
		return this;
	}

	public CLong set(long l) {
		longValue = l;
		return this;
	}

	public long get() {
		return longValue;
	}

	@Override
	public byte[] toBytes() {
		return new byte[] { LONG_CODE, (byte) ((longValue >> 56) & 0xFF),
				(byte) ((longValue >> 48) & 0xFF),
				(byte) ((longValue >> 40) & 0xFF),
				(byte) ((longValue >> 32) & 0xFF),
				(byte) ((longValue >> 24) & 0xFF),
				(byte) ((longValue >> 16) & 0xFF),
				(byte) ((longValue >> 8) & 0xFF), (byte) (longValue & 0xFF) };
	}

	@Override
	public String toString() {
		return longValue + "";
	}

	public static CLong fromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
