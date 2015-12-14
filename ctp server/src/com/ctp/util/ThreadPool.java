package com.ctp.util;

import java.util.ArrayList;

import com.ctp.io.ServerIO;
import com.ctp.server.Server;

/**
 * This static class is designed to contain all the threads produced by the server.
 * @author Calvin Gene Hall
 * @version 1.0
 *
 */

public final class ThreadPool {
	// TODO Document
	/**
	 * Gets the size of <var>THREAD_POOL</var>.
	 * @return The size of <var>THREAD_POOL</var>
	 */
	public static int getThreadCount() {
		return THREAD_POOL.size();
	}

	// TODO Document
	/**
	 * Sets <var>kill</var> to true, causing the Stalker thread to start dumping
	 * threads.
	 */
	public static void kill() {
		kill = true;
	}

	// TODO Document
	/**
	 * 
	 */
	public static void listThreads() {
		ServerIO.print("[ThreadPool] Currently Active threads: "
				+ getThreadCount());
		ServerIO.print("--------------------------------------");
		synchronized (THREAD_POOL) {
			for (Thread thread : THREAD_POOL)
				ServerIO.print("" + thread);
		}
		ServerIO.print("--------------------------------------");
	}

	// TODO Document
	/**
	 * Starts a thread, and adds it to <var>THREAD_POOL</var>.
	 * @param thread Thread to be started and added to <var>THREAD_POOL</var>
	 * @return The input thread
	 */
	public static Thread start(Thread thread) {
		thread.start();
		ServerIO.printDebug("[ThreadPool] Adding thread: " + thread);
		ServerIO.printDebug("[ThreadPool] Current threads: "
				+ THREAD_POOL.size());
		THREAD_POOL.add(thread);
		return thread;
	}
	/**
	 * The array of all the threads.
	 */
	private static final ArrayList<Thread> THREAD_POOL = new ArrayList<Thread>();
	/**
	 * The kill variable.
	 */
	private static volatile boolean kill = false;
	/**
	 * The stalker thread, which "stalks" the threads in <var>THREAD_POOL</var> and removes inactive threads.
	 */
	private static final Thread STALKER_THREAD = new Thread(new Runnable() {

		@Override
		public void run() {
			int interval = 0;
			ServerIO.print("[ThreadPool] ThreadPool is up and running.");
			while (!ThreadPool.kill) {
				synchronized (THREAD_POOL) {
					interval++;
					interval %= 32;
					if (interval == 0 && !Server.isShuttingDown())
						ServerIO.printDebug("[ThreadPool] Current threads: "
								+ THREAD_POOL.size());
					for (int i = 0; i < THREAD_POOL.size(); i++)
						if (!THREAD_POOL.get(i).isAlive()) {
							ServerIO.printDebug("[ThreadPool] Disposing of thread: "
									+ THREAD_POOL.get(i));
							THREAD_POOL.remove(i);
						}
				}
				try {
					if (!Server.isShuttingDown()) {
						synchronized (STALKER_THREAD) {
							STALKER_THREAD.wait(15000);
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			THREAD_POOL.clear();
		}

	});
	// TODO Document
	/**
	 * Starts the stalker thread.
	 */
	static {
		STALKER_THREAD.start();
	}

	// TODO Document
	/**
	 * Private constructor, to prevent instantiation.
	 */
	private ThreadPool() {
		// To prevent instantiation.
	}
}
