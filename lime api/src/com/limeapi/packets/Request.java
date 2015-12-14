package com.limeapi.packets;

import java.util.ArrayList;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class Request {
    public static final ArrayList<Request> definedRequests = new ArrayList<Request>();
    public static final Request NONE = new Request((byte) 0, "NONE");
    public final byte byteTag;
    public final String name;
    
    static {
	definedRequests.add(NONE);
    }

    private Request(final byte byteTag, final String name) {
	this.byteTag = byteTag;
	this.name = name;
    }

    public static Request getRequest(byte byteTag) {
	for (Request request : definedRequests) {
	    if (request.byteTag == byteTag) {
		return request;
	    }
	}
	return new Request(byteTag, "UNKNOWN: " + byteTag);
    }
}
