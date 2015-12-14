package user;

import client.Client;

public final class User {
	final Client CLIENT;
	public boolean loggedIn = false;
	public boolean rememberMe = false;
	public String name = "Null";
	public String usernameBox = "Username";
	public String password = "";
	public String passwordBox = "Password";
	public boolean typing = false;
	public String message = "";
	public String[] messages = new String[1000];
	public int messagePointer = 0;
	public int messageIndex = 1;
	public int playerRegionX = 1;
	public int playerRegionY = 1;
	public int playerX = -500;
	public int playerY = -500;

	public User(final Client CLIENT) {
		this.CLIENT = CLIENT;
		for (int i = 0; i < 1000; i++) {
			messages[i] = "";
		}
		playerRegionX = 1;
		playerRegionY = 1;
	}
}
