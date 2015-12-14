package com.dew.users;

import com.dew.util.BinaryOperations;
import com.dew.util.ByteFormatted;
import com.dew.util.Timer;

public class Restriction implements ByteFormatted<Restriction> {
	private final String key;
	private Timer timer = new Timer(0L);
	private boolean permanent = false;

	public Restriction(String key) {
		this.key = key;
	}

	public Restriction(String key, long timeLeft) {
		this.key = key;
		timer.setTime(timeLeft);
	}

	public void addTime(long time) {
		timer.addTime(time);
	}

	//Ignore this. It's a bit broken atm.
	@Override
	public Restriction fromBytes(byte[][] bytes) {
		String key = new String(
				BinaryOperations.byteArrayToCharacterArray(bytes[0]));
		boolean permanent = bytes[1][0] == 1 ? true : false;
		long timeLeft = BinaryOperations.bytesToLong(bytes[1], 1);
		return new Restriction(key).setPermanent(permanent).setTimeLeft(
				timeLeft);
	}

	public String getKey() {
		return key;
	}

	public boolean getPermanent() {
		return permanent;
	}

	public long getTimeLeft() {
		return timer.getTimeLeft();
	}

	public Restriction setPermanent(boolean permanent) {
		this.permanent = permanent;
		return this;
	}

	public Restriction setTimeLeft(long timeLeft) {
		timer.setTime(timeLeft);
		return this;
	}

	public boolean tick() {
		return permanent ? false : timer.tick();
	}

	@Override
	public byte[][] toBytes() {
		byte[][] bytes = new byte[2][];
		bytes[0] = BinaryOperations
				.characterArrayToByteArray(key.toCharArray());
		bytes[1] = new byte[9];
		bytes[1][0] = (byte) (permanent ? 1 : 0);
		byte[] timeBytes = BinaryOperations.toBytes(getTimeLeft());
		bytes[1][1] = timeBytes[0];
		bytes[1][2] = timeBytes[1];
		bytes[1][3] = timeBytes[2];
		bytes[1][4] = timeBytes[3];
		bytes[1][5] = timeBytes[4];
		bytes[1][6] = timeBytes[5];
		bytes[1][7] = timeBytes[6];
		bytes[1][8] = timeBytes[7];
		return bytes;
	}
}
