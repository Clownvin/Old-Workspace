package com.ctp.util;

public final class ServerClock {
	private java.lang.String lastTime;
	private java.lang.String formattedTime;
	private int hour = 0;
	private int minute = 0;
	private int second = 0;
	private volatile boolean kill = false;
	private final Thread clockManager = new Thread(new Runnable() {

		@Override
		public void run() {
			while (!kill) {
				updateTimes();
				try {
					Thread.sleep(900);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	});

	public ServerClock() {
		clockManager.start();
	}

	public synchronized java.lang.String getTime() {
		if (formattedTime == null) {
			return lastTime;
		}
		return formattedTime;
	}

	public void kill() {
		this.kill = true;
	}

	public synchronized void updateTimes() {
		lastTime = formattedTime;
		long curTime = System.currentTimeMillis();
		second = (int) ((curTime % 60000) / 1000);
		minute = (int) ((curTime % 3600000) / 60000);
		hour = (int) ((curTime % 86400000) / 3600000);
		if (hour < 10) {
			formattedTime = "0" + hour;
		} else {
			formattedTime = "" + hour;
		}
		if (minute < 10) {
			formattedTime += ":0" + minute;
		} else {
			formattedTime += ":" + minute;
		}
		if (second < 10) {
			formattedTime += ":0" + second;
		} else {
			formattedTime += ":" + second;
		}
	}
}
