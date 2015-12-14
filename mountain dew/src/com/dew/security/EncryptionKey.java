package com.dew.security;

import com.dew.util.Utilities;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class EncryptionKey {
	public static int createEndKey(long otherKey, long ourKey, long g) {
		return (int) (((long) Math.pow(otherKey, ourKey) % g) % 0xFFFFFFFFL);
	}
	public static long generateShareKey(long g, long p) {
		return Utilities.modularPow(p,
				(long) (Math.random() * 0x7FFFFFFFFFFFFFFFL), g);
	}
	private long myKey1;

	private long myKey2;

	public long g, p;

	public EncryptionKey() {
		g = (long) (Math.random() * Integer.MAX_VALUE);
		p = (long) ((0.05f + (Math.random() * .75f)) * g);

		myKey1 = generateShareKey(g, p);
		myKey2 = generateShareKey(g, p);

		System.out.println(Long.toHexString(myKey1).length() < 2 ? "0"
				+ Long.toHexString(myKey1) : Long.toHexString(myKey1));
		System.out.println(Long.toHexString(myKey2).length() < 2 ? "0"
				+ Long.toHexString(myKey2) : Long.toHexString(myKey2));

		//myKey1 = (int) (((long) Math.pow(myKey2, myKey1) % g) % 0xFFFFFFFFL);
		//myKey2 = (int) (((long) Math.pow(myKey1, myKey2) % g) % 0xFFFFFFFFL);
		System.out.println(createEndKey(myKey1, myKey2, g));
		System.out.println(createEndKey(myKey2, myKey1, g));
	}
}
