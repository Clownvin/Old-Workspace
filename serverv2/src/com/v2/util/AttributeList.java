package com.v2.util;

import java.util.HashMap;

public final class AttributeList {
    private final HashMap<String, Number> attributes = new HashMap<String, Number>();
    
    public void setEmptyAttributes(String... attributes) {
	for (String s : attributes) {
	    this.attributes.put(s, 0);
	}
    }
    
    public void setAttribute(String key, Number value) {
	this.attributes.put(key, value);
    }
    
    public Number getAttribute(String key) {
	return this.attributes.get(key);
    }
}
