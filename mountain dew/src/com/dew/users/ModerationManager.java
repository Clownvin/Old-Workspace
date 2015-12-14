package com.dew.users;

import java.util.HashMap;

import com.dew.io.ServerIO;

//TODO Think of way to Link Moderations to their accounts (Append it and the end of the file. Or something)
public final class ModerationManager extends Thread implements Runnable,
		LogoutAware {
	public static boolean addRestrictionToIP(String ip, Restriction restriction) {
		if (IP_MODERATIONS.containsKey(ip)) {
			IP_MODERATIONS.get(ip).addRestriction(restriction);
			return true;
		} else {
			Moderation moderation = new Moderation(ip, restriction);
			IP_MODERATIONS.put(ip, moderation);
			return true;
		}
	}
	public static Moderation getModerationForIP(String ip) {
		return IP_MODERATIONS.get(ip);
	}
	public static Moderation getModerationForUsername(String username) {
		return USERNAME_MODERATIONS.get(username);
	}
	public static ModerationManager getSingleton() {
		return SINGLETON;
	}
	public static void kill() {
		kill = true;
		// Save data
	}

	private static volatile boolean kill = false;

	private static final HashMap<String, Moderation> IP_MODERATIONS = new HashMap<String, Moderation>();

	private static final HashMap<String, Moderation> USERNAME_MODERATIONS = new HashMap<String, Moderation>();

	private static final ModerationManager SINGLETON = new ModerationManager();

	static {
		SINGLETON.start();
		UserInteraction.addLogoutAware(SINGLETON);
	}

	private ModerationManager() {
		// Prevent instantiation
	}

	@Override
	public void checkLogout(int userId) {
		// TODO Auto-generated method stub
		// Save profile
	}

	//Consider removing, and having the restriction/ moderation object update the player.
	//Would be cleaner.
	@Override
	public void run() {
		ServerIO.print("[ModerationManager] ModerationManager up and running.");
		while (!kill) {
			synchronized (IP_MODERATIONS) {
				for (String key : IP_MODERATIONS.keySet()) {
					if (IP_MODERATIONS.get(key).tickRestrictions()) {
						synchronized (SINGLETON) {
							SINGLETON.notifyAll();
						}
					}
				}
			}
			synchronized (USERNAME_MODERATIONS) {
				for (String key : USERNAME_MODERATIONS.keySet()) {
					if (USERNAME_MODERATIONS.get(key).tickRestrictions()) {
						synchronized (SINGLETON) {
							SINGLETON.notifyAll();
						}
					}
				}
			}
			try {
				sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
