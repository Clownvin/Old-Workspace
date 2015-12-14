package com.hexane.users;

import java.io.Serializable;

public final class UserProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8298285704420198298L;
	public String username;
	private String password;
	public int x, y, z;
	public final int ID;

	public UserProfile(final int ID) {
		this.ID = ID;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public boolean verifyPassword(String password) {
		return this.password.equalsIgnoreCase(password);
	}
}