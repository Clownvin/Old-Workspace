package com.hexane.util;

import java.util.ArrayList;

import com.hexane.server.Server;

public final class CycleProcessManager extends Thread implements Runnable {
	public final Server server;
	private volatile boolean kill = false;
	private final ArrayList<CycleProcess> processes = new ArrayList<CycleProcess>();

	public CycleProcessManager(final Server host) {
		this.server = host;
	}

	public void addProcess(CycleProcess process) {
		processes.add(process);
	}

	public void kill() {
		kill = true;
	}

	public void removeProcess(final CycleProcess process) {
		process.end();
		processes.remove(process);
	}

	@Override
	public void run() {
		server.getServerIO().print(
				"[" + this + "] CycleProcessManager is up and running.");
		while (!kill) {
			for (int i = 0; i < processes.size(); i++) {
				processes.get(i).process();
			}
			if (server.getLowCPU()) {
				try {
					sleep(1);
				} catch (InterruptedException e) {
					server.getServerIO().writeException(e);
				}
			}
		}
	}

	@Override
	public String toString() {
		return "CycleProcessManager";
	}
}
