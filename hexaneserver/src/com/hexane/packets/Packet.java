package com.hexane.packets;

import java.io.Serializable;

public abstract class Packet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5094378255744200492L;
	public Request request = Request.NULL;
	public int dataTypes = 1;
	public ServersideData serversideData = null;
	
	
}