package com.cserver.lang;

public class CCharacter implements CVariable {
	public static final char[] CHAR_ARRAY = new char[] { 'a', 'b', 'c', 'd',
			'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
			'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' ', 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', '.', ',',
			'\\', '\'', '<', '>', '{', '}', '[', ']', '(', ')', '+', '=', '-',
			'?', '/', '\"', ';', ':', '!', '@', '#', '$', '%', '^', '&', '|',
			'*', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static final byte MAX_CHAR = 126;

	protected byte character = -1;

	public CCharacter(char c) {
		character = getByteForChar(c);
	}

	public CCharacter(byte b) {
		b = (byte) (b & 0x7F);
		if (b > -1 && b <= MAX_CHAR)
			character = b;
	}

	public byte toByte() {
		return character > -1 && character <= MAX_CHAR ? character : -1;
	}

	public static byte getByteForChar(char c) {
		for (byte b = 0; b < MAX_CHAR; b++) {
			if (CHAR_ARRAY[b] == c) {
				return b;
			}
		}
		return -1;
	}

	@Override
	public String toString() {
		if (character > -1 && character <= MAX_CHAR)
			return CHAR_ARRAY[character] + "";
		return "";
	}

	public char toChar() {
		if (character > -1 && character <= MAX_CHAR)
			return CHAR_ARRAY[character];
		return 'ß';
	}

	@Override
	public byte[] toBytes() {
		return new byte[] { CHARACTER_CODE,
				character > -1 && character <= MAX_CHAR ? character : -1 };
	}

	@Override
	public CCharacter fromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CCharacter fromBytes(byte[] bytes, int start) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CCharacter[] arrayFromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CCharacter[] arrayFromBytes(byte[] bytes, int start) {
		// TODO Auto-generated method stub
		return null;
	}
}
