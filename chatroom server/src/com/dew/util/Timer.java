package com.dew.util;

public final class Timer extends Thread implements Runnable {
	private long duration = 0L;
	private long elapsedTime = 0L;
	private long lastTime = 0L;
	private volatile boolean finished;

	public Timer(long duration) {
		this.duration = duration;
	}

	public void addTime(long time) {
		duration += time;
	}

	public long getDuration() {
		return duration;
	}

	public long getTimeLeft() {
		tick();
		return duration - elapsedTime;
	}

	public boolean isFinished() {
		return finished;
	}

	public void resetTimer() {
		elapsedTime = 0;
		finished = false;
	}

	@Override
	public void run() {
		while (!tick()) {
			try {
				sleep(getTimeLeft());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.notifyAll();
	}

	public void setTime(long duration) {
		this.duration = duration;
	}

	public void startTimer() {
		this.start();
	}

	public boolean tick() {
		if (!finished) {
			elapsedTime += System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			finished = elapsedTime >= duration;
		}
		return finished;
	}
}
