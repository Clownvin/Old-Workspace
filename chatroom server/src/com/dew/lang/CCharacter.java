package com.dew.lang;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class CCharacter {

	public static byte getByteForChar(char c) {
		for (byte b = 0; b < MAX_CHAR; b++) {
			if (CHAR_ARRAY[b] == c) {
				return b;
			}
		}
		return -1;
	}

	public static final char[] CHAR_ARRAY = new char[] { 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', '.', ',',
			'\\', '\'', '<', '>', '{', '}', '[', ']', '(', ')', '+', '=', '-',
			'?', '/', '\"', ';', ':', '!', '@', '#', '$', '%', '^', '&', '|',
			'*', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

	public static final byte MAX_CHAR = 127;

	protected byte character = -1;

	public CCharacter(byte b) {
		b = (byte) (b & 0x7F);
		if (b > -1 && b <= MAX_CHAR)
			character = b;
	}

	public CCharacter(char c) {
		character = getByteForChar(c);
	}

	public byte getByte() {
		return character;
	}

	public char toChar() {
		return CHAR_ARRAY[character];
	}

	@Override
	public java.lang.String toString() {
		if (character == -1)
			return "NULL";
		return CHAR_ARRAY[character] + "";
	}
}
