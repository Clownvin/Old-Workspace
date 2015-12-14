package com.hexane.packets;

import java.io.Serializable;

public enum Request implements Serializable {
	NULL, ATTEMPT_LOGIN, SUCCESSFUL_LOGIN, ERROR_LOGIN, LOGOUT, CLICK, CLICK_OBJECT, ALT_CLICK, MOVE_KEY_PRESSED, CHAT_MESSAGE, WHISPER;

	@Override
	public String toString() {
		switch (this) {
		case NULL:
			return "NULL";
		case ATTEMPT_LOGIN:
			return "ATTEMPT_LOGIN";
		case SUCCESSFUL_LOGIN:
			return "SUCCESSFUL_LOGIN";
		case ERROR_LOGIN:
			return "ERROR_LOGIN";
		case LOGOUT:
			return "LOGOUT";
		case CLICK:
			return "CLICK";
		case CLICK_OBJECT:
			return "CLICK_OBJECT";
		case ALT_CLICK:
			return "ALT_CLICK";
		case MOVE_KEY_PRESSED:
			return "MOVE_KEY_PRESSED";
		case CHAT_MESSAGE:
			return "CHAT_MESSAGE";
		case WHISPER:
			return "WHISPER";
		default:
			return "Unknown";
		}
	}
}
