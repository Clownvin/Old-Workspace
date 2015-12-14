package com.dew.packets;

import java.io.Serializable;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public enum Request implements Serializable {
	NULL((byte) 0), ATTEMPT_LOGIN((byte) 1), SUCCESSFUL_LOGIN((byte) 2), ERROR_LOGIN(
			(byte) 3), LOGOUT((byte) 4), WARNING((byte) 5), SUCCESSFUL_LOGOUT(
			(byte) 6), MESSAGE((byte) 7), ATTRIBUTE_GET((byte) 8) { 
		public final int GET_MAX_USERNAME_CHARS = 1;} ;
	public static Request getRequest(byte b) {
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
		case 5:
			return WARNING;
		case 6:
			return SUCCESSFUL_LOGOUT;
		case 7:
			return MESSAGE;
		default:
			System.out.println("Unknown requestID: " + b);
			return NULL;
		}
	}

	private final byte BYTE_TAG;

	private Request(byte BYTE_TAG) {
		this.BYTE_TAG = BYTE_TAG;
	}

	public byte getByteTag() {
		return BYTE_TAG;
	}
}
