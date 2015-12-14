package com.ctp.lang;

import java.util.ArrayList;

public class String {
	protected ArrayList<Character> characterList = new ArrayList<Character>();
	
	public String() {
		// Just for the sake of having it.
	}
	
	public String(byte[] bytes) {
		for (byte b : bytes) {
			characterList.add(new Character(b));
		}
	}
	
	public String(ArrayList<Byte> bytes) {
		for (byte b : bytes) {
			characterList.add(new Character(b));
		}
	}
	
	public String(java.lang.String string) {
		for (int i = 0; i < string.length(); i++) {
			characterList.add(new Character(string.charAt(i)));
		}
	}
	
	//PERFORMANCE Consider generating String s outside, and only sending reference, instead of looping through each time.
	@Override
	public java.lang.String toString() {
		java.lang.String s = "";
		for (Character c : characterList) {
			s += c;
		}
		return s;
	}
	
	public int length() {
		return characterList.size();
	}
	
	protected ArrayList<Character> getCharacterList() {
		return characterList;
	}
	
	public String add(String string) {
		characterList.addAll(string.getCharacterList());
		return this;
	}
	
	public String set(String string) {
		characterList = string.getCharacterList();
		return this;
	}
	
	public String add(java.lang.String string) {
		ArrayList<Character> stringChars = new ArrayList<Character>();
		for(int i = 0; i < string.length(); i++) {
			stringChars.add(new Character(string.charAt(i)));
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
