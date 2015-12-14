package com.ctp.packets;

import java.io.Serializable;

public enum Request implements Serializable {
	NULL("Null", (byte) 0), ATTEMPT_LOGIN("Attempt Login", (byte) 1), SUCCESSFUL_LOGIN("Successful Login", (byte) 2), ERROR_LOGIN("Error Login", (byte) 3), LOGOUT("Logout", (byte) 4);
	private final String string;
	private final byte byteID;
	
	
	private Request(String string, byte byteID) {
		this.string = string;
		this.byteID = byteID;
	}
	@Override
	public String toString() {
		return string;
	}
	
	public byte getByteID() {
		return byteID;
	}
	
	public static Request getRequest(byte b) {
		System.out.println("getRequest("+b+"):Server");
		switch (b) {
		case 0:
			return NULL;
		case 1:
			return ATTEMPT_LOGIN;
		case 2:
			return SUCCESSFUL_LOGIN;
		case 3:
			return ERROR_LOGIN;
		case 4:
			return LOGOUT;
		default:
			System.out.println("Unknown requestID: "+b);
			return NULL;
		}
	}
}
