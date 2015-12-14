package com.v2.io;

import com.v2.util.ServerClock;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ServerIO {

    public static ServerIO getSingleton() {
	return SINGLETON;
    }

    public static void newLine() {
	System.out.println("");
	if (runtimeLog) {
	    logger.write("");
	}
    }

    public static void print(String s) {
	if (showTime) {
	    System.out.println("[" + ServerClock.getTime() + "]" + s);
	} else {
	    System.out.println(s);
	}
	if (runtimeLog) {
	    if (showTime)
		logger.write("[" + ServerClock.getTime() + "]" + s);
	    else
		logger.write(s);
	}
    }

    public static void print(String s, int[] channels) {
	if (showTime) {
	    System.out.println("[" + ServerClock.getTime() + "]" + s);
	} else {
	    System.out.println(s);
	}
	if (runtimeLog) {
	    if (showTime)
		logger.write("[" + ServerClock.getTime() + "]" + s);
	    else
		logger.write(s);
	}
    }

    public static void printDebug(String s) {
	if (debug) {
	    if (showTime) {
		System.out.println("[" + ServerClock.getTime() + "][<DEBUG>]"
			+ s);
	    } else {
		System.out.println("[<DEBUG>]" + s);
	    }
	    if (runtimeLog) {
		if (showTime)
		    logger.write("[" + ServerClock.getTime() + "][<DEBUG>]" + s);
		else
		    logger.write("[<DEBUG>]" + s);
	    }
	}
    }

    public static void printErr(String s) {
	if (showTime) {
	    System.err.println("[" + ServerClock.getTime() + "]" + s);
	} else {
	    System.err.println(s);
	}
	if (runtimeLog) {
	    if (showTime)
		logger.write("[" + ServerClock.getTime() + "]" + s);
	    else
		logger.write(s);
	}
    }

    public static void printErr(String s, int[] channels) {
	if (showTime) {
	    System.err.println("[" + ServerClock.getTime() + "]" + s);
	} else {
	    System.err.println(s);
	}
	if (runtimeLog) {
	    if (showTime)
		logger.write("[" + ServerClock.getTime() + "]" + s);
	    else
		logger.write(s);
	}
    }

    /*
     * Decomissioned for now.. public static void sendPacket(int serverIndex,
     * int connectionId, Packet packet) { try {
     * SubServerManager.get(serverIndex).getConnection(connectionId)
     * .queueOutgoingPacket(packet); } catch (InvalidIdentifierException e) {
     * printErr("[ServerIO] Exception, ID " + connectionId +
     * " is an invalid identifier."); writeException(e); } }
     */

    public static void setRuntimeLog(final boolean state) {
	runtimeLog = state;
    }

    public static void setShowTime(final boolean state) {
	showTime = state;
    }

    public static void setDebug(final boolean state) {
	debug = state;
    }

    public static void writeException(Exception e) {
	if (debug) {
	    if (runtimeLog) {
		e.printStackTrace(logger.getPrintWriter());
		logger.flush();
	    }
	    e.printStackTrace();
	}
    }

    private static boolean runtimeLog = false;

    private static boolean showTime = false;

    private static boolean debug = false;

    private static Logger logger = null;

    private static final ServerIO SINGLETON = new ServerIO();

    static {
	logger = new Logger();
    }

    private ServerIO() {
	// To prevent instantiation.
    }

    public boolean getShowTime() {
	return showTime;
    }

    @Override
    public String toString() {
	return "ServerIO";
    }
}
