package com.ctp.util;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ctp.io.ServerIO;
import com.ctp.server.Server;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class CycleProcessManager extends Thread implements Runnable {
	public static void addProcess(CycleProcess process) {
		CYCLE_PROCESSES.add(process);
	}
	
	public double getAverageTimeMilliseconds() {
		double avg = (cumulativeTime/cycles) / 1000000.0D;
		return avg;
	}
	
	public double getAverageTimeMilliseconds(boolean clear) {
		double avg = (cumulativeTime/cycles) / 1000000.0D;
		if (clear) {
			cycles = 0;
			cumulativeTime = 0;
		}
		return avg;
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
		ThreadPool.start(SINGLETON);
	}

	private CycleProcessManager() {
		// To prevent instantiation.
	}

	@Override
	public void run() {
		DECIMAL_FORMAT.setMaximumFractionDigits(8);
		ServerIO.print("[CycleProcessManager] CycleProcessManager is up and running.");
		while (!kill) {
			for (int i = 0; i < CYCLE_PROCESSES.size(); i++) {
				CYCLE_PROCESSES.get(i).process();
			}
			if (lastTime != 0) {
				cumulativeTime += System.nanoTime() - lastTime;
				cycles++;
			}
			lastTime = System.nanoTime();
			if ((System.nanoTime() - startOfBlockTime) / 60000000000.0D >= 5) {
				startOfBlockTime = System.nanoTime();
				ServerIO.print("["+this+"] Average cycle time over 5 minutes: "+DECIMAL_FORMAT.format(getAverageTimeMilliseconds(true))+"ms.");
			}
			if (Server.getLowCPU()) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
					ServerIO.writeException(e);
				}
			}
		}
		ServerIO.printDebug("[CycleProcessManager] CycleProcessManager was killed.");
	}

	@Override
	public String toString() {
		return "CycleProcessManager";
	}
}
