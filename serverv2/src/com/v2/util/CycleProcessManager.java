package com.v2.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.v2.io.ServerIO;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class CycleProcessManager extends Thread implements Runnable {
    public static void addProcess(CycleProcess process) {
	CYCLE_PROCESSES.add(process);
	synchronized (SINGLETON) {
	    SINGLETON.notify();
	}
    }

    public static CycleProcessManager getSingleton() {
	return SINGLETON;
    }

    public static void kill() {
	kill = true;
    }

    public static void removeProcess(final CycleProcess process) {
	process.end();
	CYCLE_PROCESSES.remove(process);
    }

    private static volatile boolean kill = false;

    private static long lastTime = 0L;

    private static long startOfBlockTime = System.nanoTime();

    private static long cumulativeTime = 0;

    private static long cycles = 0;

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#");

    private static final ArrayList<CycleProcess> CYCLE_PROCESSES = new ArrayList<CycleProcess>();

    private static final CycleProcessManager SINGLETON = new CycleProcessManager();

    static {
	SINGLETON.start();
    }

    private CycleProcessManager() {
	// To prevent instantiation.
    }

    public double getAverageTimeMilliseconds() {
	double avg = (cumulativeTime / cycles) / 1000000.0D;
	return avg;
    }

    public double getAverageTimeMilliseconds(boolean clear) {
	double avg = (cumulativeTime / cycles) / 1000000.0D;
	if (clear) {
	    cycles = 0;
	    cumulativeTime = 0;
	}
	return avg;
    }

    @Override
    public void run() {
	DECIMAL_FORMAT.setMaximumFractionDigits(8);
	// Oscillate
	boolean oscillationFlag = false;
	ServerIO.print("[CycleProcessManager] CycleProcessManager is up and running.");
	while (!kill) {
	    while (CYCLE_PROCESSES.size() == 0)
		synchronized (SINGLETON) {
		    try {
			SINGLETON.wait();
		    } catch (InterruptedException e) {
			ServerIO.writeException(e);
		    }
		}
	    oscillationFlag = !oscillationFlag;
	    if (oscillationFlag) {
		synchronized (CYCLE_PROCESSES) {
		    for (int i = 0; i < CYCLE_PROCESSES.size(); i++) {
			if (CYCLE_PROCESSES.get(i) != null) {
			    if (CYCLE_PROCESSES.get(i).endConditionMet()) {
				CYCLE_PROCESSES.get(i).end();
				ServerIO.printDebug("[CycleProcessManager] Removing CycleProcess: "
					+ CYCLE_PROCESSES.get(i));
				CYCLE_PROCESSES.remove(i);
			    } else {
				CYCLE_PROCESSES.get(i).process();
			    }
			}
		    }
		}
	    } else {
		synchronized (CYCLE_PROCESSES) {
		    for (int i = CYCLE_PROCESSES.size() - 1; i > -1; i--) {
			if (CYCLE_PROCESSES.get(i) != null) {
			    if (CYCLE_PROCESSES.get(i).endConditionMet()) {
				CYCLE_PROCESSES.get(i).end();
				ServerIO.printDebug("[CycleProcessManager] Removing CycleProcess: "
					+ CYCLE_PROCESSES.get(i));
				CYCLE_PROCESSES.remove(i);
			    } else {
				CYCLE_PROCESSES.get(i).process();
			    }
			}
		    }
		}
	    }
	    if (lastTime != 0) {
		cumulativeTime += System.nanoTime() - lastTime;
		cycles++;
	    }
	    lastTime = System.nanoTime();
	    if ((System.nanoTime() - startOfBlockTime) / 60000000000.0D >= 5) {
		startOfBlockTime = System.nanoTime();
		ServerIO.print("["
			+ this
			+ "] Average cycle time over 5 minutes: "
			+ DECIMAL_FORMAT
				.format(getAverageTimeMilliseconds(true))
			+ "ms.");
	    }
	}
	ServerIO.printDebug("[CycleProcessManager] CycleProcessManager was killed.");
    }

    @Override
    public String toString() {
	return "CycleProcessManager";
    }
}
