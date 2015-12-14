package com.hexane.packets;

import java.io.Serializable;

public final class PacketData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2162725162338702182L;
	public DataType type = null;
	public boolean array = false;
	public Object o = null;
}
