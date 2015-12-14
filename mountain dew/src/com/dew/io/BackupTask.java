package com.dew.io;

import java.io.File;
import java.util.Date;

import com.dew.threading.ThreadTask;
import com.dew.users.UserProfile;

//TODO Add structured manifest of backups, and delete the last day backup after 14 days. 3 Month backups too, delete the 4th once new is added.
public final class BackupTask implements ThreadTask {
	private volatile boolean killed = false;
	private static final int daysSinceModToKeep = 30; // 30 days till it won't backup file.
	
	private static final double totalLevelToDay =  0.147474747D;
	
	@Override 
	public boolean reachedEnd() {
		return killed;
	}
	
	@Override
	public int getPriority() {
		return 1;
	}

	@Override
	public void doTask() {
		try {
			Thread.sleep(3600000); // 1 more 0 = 10 hour
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ServerIO.print("[Backup] Backup starting...");
		File userFolder = new File("./Data/Users/");
		if (!userFolder.exists()) {
			throw new RuntimeException(
					"No user folder for BackupTask to back up!");
		}
		for (File user : userFolder.listFiles()) {
			if (user != null) {
				if (new Date().getTime() - user.lastModified() < 86400000L * daysSinceModToKeep) {
					FileManager.writeBackupFile(FileManager
						.readFile(new UserProfile(0),
								user.getName().replace(".user", "")));
				} else {
					System.out.println("Old file "+user.getName()+", not backing up and deleting.");
					System.out.println("Delete was "+user.delete());
				}
			}
		}
		ServerIO.print("[Backup] Backup finished.");
	}

	@Override
	public void end() {
		ServerIO.printErr("[Backup] Task killed.");
		killed = true;
	}

}
