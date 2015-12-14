package com.v2.lang;

import java.util.ArrayList;

import com.v2.lang.CCharacter;
import com.v2.lang.CString;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class CString {
    protected ArrayList<CCharacter> characterList = new ArrayList<CCharacter>();

    public CString() {
	// Just for the sake of having it.
    }

    public CString(ArrayList<Byte> bytes) {
	for (byte b : bytes) {
	    characterList.add(new CCharacter(b));
	}
    }

    public CString(byte[] bytes) {
	for (byte b : bytes) {
	    characterList.add(new CCharacter(b));
	}
    }

    public CString(java.lang.String string) {
	for (int i = 0; i < string.length(); i++) {
	    characterList.add(new CCharacter(string.charAt(i)));
	}
    }

    public CString add(CString string) {
	characterList.addAll(string.getCharacterList());
	return this;
    }

    public CString add(java.lang.String string) {
	ArrayList<CCharacter> stringChars = new ArrayList<CCharacter>();
	for (int i = 0; i < string.length(); i++) {
	    stringChars.add(new CCharacter(string.charAt(i)));
	}
	characterList.addAll(stringChars);
	return this;
    }

    protected ArrayList<CCharacter> getCharacterList() {
	return characterList;
    }

    public int length() {
	return characterList.size();
    }

    public CString set(CString string) {
	characterList = string.getCharacterList();
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

    // PERFORMANCE Consider generating String s outside, and only sending
    // reference, instead of looping through each time.
    @Override
    public java.lang.String toString() {
	java.lang.String s = "";
	for (CCharacter c : characterList) {
	    s += c;
	}
	return s;
    }
}
