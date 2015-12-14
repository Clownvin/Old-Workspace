package com.limeapi.connection;

public final class ConnectionAddress {
    private final int connectionID;
    private int subServerIndex;
    
    public ConnectionAddress(final int connectionID, final int subServerIndex) {
	this.connectionID = connectionID;
	this.subServerIndex = subServerIndex;
    }
    
    public ConnectionAddress setSubServerIndex(final int subServerIndex) {
	this.subServerIndex = subServerIndex;
	return this;
    }
    
    public int getConnectionID() {
	return connectionID;
    }
    
    public int getSubServerIndex() {
	return subServerIndex;
    }
    
    @Override
    public String toString() {
	return subServerIndex+"."+connectionID;
    }
}
