package com.v2.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.v2.io.ServerIO;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ServerClock extends Thread implements Runnable {
    public static ServerClock getSingleton() {
	return SINGLETON;
    }

    public synchronized static java.lang.String getTime() {
	if (formattedTime == null) {
	    return lastTime;
	}
	return formattedTime;
    }

    public static void kill() {
	kill = true;
    }

    public synchronized static void updateTimes() {
	lastTime = formattedTime;
	formattedTime = new SimpleDateFormat("hh:mm:ss aa").format(Calendar
		.getInstance().getTime());
    }

    private static String lastTime;
    private static String formattedTime;

    private static volatile boolean kill = false;

    private static final ServerClock SINGLETON = new ServerClock();

    static {
	SINGLETON.start();
    }

    private ServerClock() {
	// To prevent instantiation.
    }

    @Override
    public void run() {
	while (!ServerClock.kill) {
	    updateTimes();
	    try {
		Thread.sleep(900);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
	ServerIO.printDebug("[" + this + "] Server clock was killed.");
    }

    @Override
    public String toString() {
	return "ServerClock";
    }
}
