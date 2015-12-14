package com.dew.packets;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public enum Protocall {
	NONE((byte) 0, (byte) 0), FILE((byte) 1, (byte) 10), CHAT((byte) 2,
			(byte) 15), GENERAL((byte) 3, (byte) 20), URGENT((byte) 4,
			Byte.MAX_VALUE);
	public static Protocall getProtocal(byte byteTag) {
		switch (byteTag) {
		case 0:
			return NONE;
		case 1:
			return FILE;
		case 2:
			return CHAT;
		case 3:
			return GENERAL;
		case 4:
			return URGENT;
		default:
			System.out.println("Unkown protocall: 0x"
					+ Integer.toHexString(Byte.toUnsignedInt(byteTag)));
			return NONE;
		}
	}

	private final byte BYTE_TAG;
	private final byte PRIORITY;

	private Protocall(byte byteTag, byte priority) {
		this.BYTE_TAG = byteTag;
		this.PRIORITY = priority;
	}

	public byte getByteTag() {
		return BYTE_TAG;
	}

	public byte getPriority() {
		return PRIORITY;
	}
}
