package com.ctp.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

import com.ctp.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class FileManagerWriter extends BufferedWriter {
	private final String writerID;
	private final long writerByteID;

	public FileManagerWriter(Writer arg0, final String id) {
		super(arg0);
		writerID = id;
		char[] idChars = new char[8];
		char[] chars = id.toCharArray();
		for (int i = 0; i < 8; i++) {
			if (i < chars.length)
				idChars[i] = chars[i];
			else
				idChars[i] = '0';
		}
		writerByteID = BinaryOperations.bytesToLong(BinaryOperations
				.characterArrayToByteArray(idChars));
	}

	public long getByteID() {
		return writerByteID;
	}

	public String getStringID() {
		return writerID;
	}

	public void write(byte b) {
		try {
			this.write(BinaryOperations
					.byteArrayToCharacterArray(BinaryOperations.toBytes(b)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(int thisInteger) {
		try {
			this.write(BinaryOperations
					.byteArrayToCharacterArray(BinaryOperations
							.toBytes(thisInteger)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(long thisLong) {
		try {
			this.write(BinaryOperations
					.byteArrayToCharacterArray(BinaryOperations
							.toBytes(thisLong)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(short s) {
		try {
			this.write(BinaryOperations
					.byteArrayToCharacterArray(BinaryOperations.toBytes(s)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void write(String message) {
		byte[] bytes = new byte[message.length()];
		char[] chars = message.toCharArray();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) chars[i];
		}
		try {
			this.write(BinaryOperations.byteArrayToCharacterArray(bytes));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
