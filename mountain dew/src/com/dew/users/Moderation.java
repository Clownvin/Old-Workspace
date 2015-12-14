package com.dew.users;

import java.util.ArrayList;

import com.dew.io.FileManagerWriteable;
import com.dew.io.FileType;
import com.dew.util.BinaryOperations;
import com.dew.util.ByteFormatted;
import com.dew.util.CycleProcess;

//TODO Loading steps:
/*
 * Read file
 * If not exist, ignore (Do for both IP and username
 * Otherwise load this
 * 
 * **Other**
 * 
 * 
 */
public final class Moderation implements CycleProcess, FileManagerWriteable {
	private static final FileType FILE_TYPE = FileType.MODERATION;
	private final String key;
	private final ArrayList<Restriction> RESTRICTIONS = new ArrayList<Restriction>();

	public Moderation(String key) {
		this.key = key;
	}

	public Moderation(String key, Restriction... restrictions) {
		this.key = key;
		for (Restriction restriction : restrictions)
			addRestriction(restriction);
	}

	public boolean addRestriction(Restriction restriction) {
		return false;
	}

	@Override
	public void end() {
		// TODO Auto-generated method stub
		// Remove the file, if it exists. We don't need it anymore.
	}

	@Override
	public boolean endConditionMet() {
		return RESTRICTIONS.size() == 0;
	}

	@Override
	public FileManagerWriteable fromBytes(byte[][] bytes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileName() {
		return key;
	}

	@Override
	public FileType getFileType() {
		return FILE_TYPE;
	}

	public String getKey() {
		return key;
	}

	@Override
	public void process() {
		if (tickRestrictions()) { // One of them reached the end of it's timer.
			for (Restriction restriction : RESTRICTIONS) {

			}
		}
	}

	public boolean tickRestrictions() {
		boolean returnBoolean = false;
		for (Restriction restriction : RESTRICTIONS) {
			if (!returnBoolean)
				returnBoolean = restriction.tick();
			else
				restriction.tick();
		}
		return returnBoolean;
	}

	//TEST Needs testing
	@Override
	public byte[][] toBytes() {
		byte[][] bytes = new byte[1 + (RESTRICTIONS.size() * 2)][];
		bytes[0] = BinaryOperations
				.characterArrayToByteArray(key.toCharArray());
		synchronized (RESTRICTIONS) {
			for (int i = 1; i < RESTRICTIONS.size(); i += 2) {
				byte[][] restrictionBytes = RESTRICTIONS.get(i).toBytes();
				bytes[i] = restrictionBytes[0];
				bytes[i + 1] = restrictionBytes[1];
			}
		}
		return bytes;
	}
}
