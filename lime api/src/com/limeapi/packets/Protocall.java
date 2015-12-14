package com.limeapi.packets;

import java.util.ArrayList;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class Protocall {
    public static final ArrayList<Protocall> definedProtocalls = new ArrayList<Protocall>();
    public static final Protocall NONE = new Protocall((byte) 0, "NONE",
	    (byte) 0);
    public static final Protocall URGENT = new Protocall((byte) 1, "URGENT",
	    Byte.MAX_VALUE);
    public static final Protocall USERS = new Protocall((byte) 2, "USERS", (byte) 0);
    public final byte byteTag;
    public byte priority;
    public final String name;
    
    static {
	definedProtocalls.add(NONE);
    }

    private Protocall(byte byteTag, String name, byte priority) {
	this.byteTag = byteTag;
	this.name = name;
	this.priority = priority;
    }

    public byte getPriority() {
	return priority;
    }

    public Protocall setPriority(final byte priority) {
	this.priority = priority;
	return this;
    }

    public static Protocall getProtocall(byte byteTag) {
	for (Protocall protocall : definedProtocalls) {
	    if (protocall.byteTag == byteTag) {
		return protocall;
	    }
	}
	return new Protocall(byteTag, "UNKNOWN: " + byteTag, (byte) 0);
    }
}
