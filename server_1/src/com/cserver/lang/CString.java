package com.cserver.lang;

import java.util.ArrayList;

public class CString implements CVariable {
	protected ArrayList<CCharacter> characterList = new ArrayList<CCharacter>();

	public CString() {
		// Just for the sake of having it.
	}

	public CString(byte[] bytes) {
		for (byte b : bytes) {
			characterList.add(new CCharacter(b));
		}
	}

	public CString(ArrayList<Byte> bytes) {
		for (byte b : bytes) {
			characterList.add(new CCharacter(b));
		}
	}

	public CString(String string) {
		for (int i = 0; i < string.length(); i++) {
			characterList.add(new CCharacter(string.charAt(i)));
		}
	}

	// PERFORMANCE Consider generating String s outside, and only sending
	// reference, instead of looping through each time.
	@Override
	public String toString() {
		String s = "";
		for (CCharacter c : characterList) {
			s += c;
		}
		return s;
	}

	public int length() {
		return characterList.size();
	}

	protected ArrayList<CCharacter> getCharacterList() {
		return characterList;
	}

	public CString add(CString string) {
		characterList.addAll(string.getCharacterList());
		return this;
	}

	public CString set(CString string) {
		characterList = string.getCharacterList();
		return this;
	}

	public CString add(String string) {
		ArrayList<CCharacter> stringChars = new ArrayList<CCharacter>();
		for (int i = 0; i < string.length(); i++) {
			stringChars.add(new CCharacter(string.charAt(i)));
		}
		characterList.addAll(stringChars);
		return this;
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[characterList.size() + 1];
		bytes[0] = STRING_CODE;
		for (int i = 0; i < bytes.length; i++) {
			bytes[i + 1] = (byte) (characterList.get(i).toByte() + ((i + 1 != bytes.length ? 0
					: 1) << 7));
		}
		return bytes;
	}

	public static CString fromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	public static CString[] arrayFromBytes(byte[] bytes) {
		// TODO Auto-generated method stub
		return null;
	}
}
