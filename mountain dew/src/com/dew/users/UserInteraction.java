package com.dew.users;

import java.util.ArrayList;

public final class UserInteraction {
	public static void addLogoutAware(LogoutAware logoutAware) {
		LOGOUT_AWARE.add(logoutAware);
	}

	public static void notifyLogout(int userId) {
		for (LogoutAware logoutAware : LOGOUT_AWARE) {
			logoutAware.checkLogout(userId);
		}
	}

	private static final ArrayList<LogoutAware> LOGOUT_AWARE = new ArrayList<LogoutAware>();
}
