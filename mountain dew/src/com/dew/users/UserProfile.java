package com.dew.users;

import com.dew.io.FileManagerWriteable;
import com.dew.io.FileType;
import com.dew.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class UserProfile implements FileManagerWriteable {
	private static final FileType FILE_TYPE = FileType.USER;
	public String username = "None";
	private String password = "None";
	public int x = 0, y = 0, z = 0;
	public final int ID;

	public UserProfile(final int ID) {
		this.ID = ID;
	}

	public UserProfile(final int ID, String username, String password, int x,
			int y, int z) {
		this.ID = ID;
		this.username = username;
		this.password = password;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public UserProfile fromBytes(byte[][] bytes) {
		String newUsername = new String(
				BinaryOperations.byteArrayToCharacterArray(bytes[0]));
		String newPassword = new String(
				BinaryOperations.byteArrayToCharacterArray(bytes[1]));
		int nX = 0;
		int nY = 0;
		int nZ = 0;
		int nID = 0;
		int state = 0;
		byte[] temp = new byte[4];
		for (int i = 0; i < bytes[2].length; i++) {
			temp[i % 4] = bytes[2][i];
			if ((i + 1) % 4 == 0)
				switch (state) {
				case 0:
					nX = BinaryOperations.bytesToInteger(temp);
					state++;
					break;
				case 1:
					nY = BinaryOperations.bytesToInteger(temp);
					state++;
					break;
				case 2:
					nZ = BinaryOperations.bytesToInteger(temp);
					state++;
					break;
				case 3:
					nID = BinaryOperations.bytesToInteger(temp);
					state++;
					break;
				}
		}

		return new UserProfile(nID, newUsername, newPassword, nX, nY, nZ);
	}

	@Override
	public String getFileName() {
		return username.substring(0, 1).toUpperCase() + (username.substring(1));
	}

	@Override
	public FileType getFileType() {
		return FILE_TYPE;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public byte[][] toBytes() {
		byte[][] bytes = new byte[3][];
		bytes[0] = BinaryOperations.characterArrayToByteArray(username
				.toCharArray());
		bytes[1] = BinaryOperations.characterArrayToByteArray(password
				.toCharArray());
		bytes[2] = new byte[16];
		int index = 0;
		for (byte b : BinaryOperations.toBytes(x)) {
			bytes[2][index] = b;
			index++;
		}
		for (byte b : BinaryOperations.toBytes(y)) {
			bytes[2][index] = b;
			index++;
		}
		for (byte b : BinaryOperations.toBytes(z)) {
			bytes[2][index] = b;
			index++;
		}
		for (byte b : BinaryOperations.toBytes(ID)) {
			bytes[2][index] = b;
			index++;
		}
		return bytes;
	}

	public boolean verifyPassword(String password) {
		return this.password.equalsIgnoreCase(password);
	}
}