package com.ctp.lang;

import java.util.ArrayList;

public class CString {
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
	
	public CString(java.lang.String string) {
		for (int i = 0; i < string.length(); i++) {
			characterList.add(new CCharacter(string.charAt(i)));
		}
	}
	
	//PERFORMANCE Consider generating String s outside, and only sending reference, instead of looping through each time.
	@Override
	public java.lang.String toString() {
		java.lang.String s = "";
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
	
	public CString add(CString cString) {
		characterList.addAll(cString.getCharacterList());
		return this;
	}
	
	public CString set(CString cString) {
		characterList = cString.getCharacterList();
		return this;
	}
	
	public CString add(java.lang.String string) {
		ArrayList<CCharacter> stringChars = new ArrayList<CCharacter>();
		for(int i = 0; i < string.length(); i++) {
			stringChars.add(new CCharacter(string.charAt(i)));
		}
		characterList.addAll(stringChars);
		return this;
	}
	
	public byte[] toBytes() {
		byte[] bytes = new byte[characterList.size()];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = characterList.get(i).character;
			if (i + 1 != bytes.length) {
				bytes[i] = (byte) (bytes[i] + (1 << 7));
			}
		}
		return bytes;
	}
}
