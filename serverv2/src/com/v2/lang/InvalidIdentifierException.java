package com.v2.lang;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class InvalidIdentifierException extends RuntimeException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7722027039547165417L;

    public InvalidIdentifierException(String string) {
	super(string);
    }

}
