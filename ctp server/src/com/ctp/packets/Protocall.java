package com.ctp.packets;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public enum Protocall {
	NONE((byte)0), FILE((byte)1), CHAT((byte)2), GENERAL((byte)3);
	private final byte BYTE_TAG;
	
	private Protocall(byte byteTag) {
		this.BYTE_TAG = byteTag;
	}
	
	public byte getByteTag() {
		return BYTE_TAG;
	}
	
	public static Protocall getProtocal(byte byteTag) {
		switch(byteTag) {
		case 0:
			return NONE;
		case 1:
			return FILE;
		case 2:
			return CHAT;
		case 3:
			return GENERAL;
		default:
			System.out.println("Unkown protocall: "+byteTag);
			return NONE;
		}
	}
}
