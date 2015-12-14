package com.hexane.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import com.hexane.packets.DataType;
import com.hexane.packets.Request;
import com.hexane.packets.SingleDataPacket;
import com.hexane.server.InvalidIdentifierException;
import com.hexane.server.Server;
import com.hexane.util.RecentIDList;

public final class UserDatabase extends Thread implements Runnable {
	private volatile boolean kill = false;
	private int nextUserID = 0;
	private final Server server;
	private final ArrayList<UserProfile> users = new ArrayList<UserProfile>();
	private RecentIDList ridl = new RecentIDList(100, 75);

	public UserDatabase(final Server server) {
		this.server = server;
		this.start();
	}

	public void addProfile(String username, String password) {
		UserProfile newUser = new UserProfile(nextUserID);
		nextUserID++;
		newUser.username = username;
		newUser.setPassword(password);
		users.add(newUser);
		server.getServerIO().print(
				"[" + this + "] Added new user " + username + " with ID "
						+ (nextUserID - 1) + ".");
	}

	public boolean closeProfile(int id) {
		if (id == -1)
			return true;
		int index = -1;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e) {
			server.getServerIO().printErr(
					"[" + this + "] Exception, user by ID " + id
							+ " does not exist.");
			server.getServerIO().writeException(e);
			return false;
		}
		try {
			if (server.getSubServerManager().getConnection(id).isLoggedIn()) {
				saveProfile(id);
				server.getServerIO().print(
						"[" + this + "] " + users.get(index).username
								+ "'s profile closed successfully.");
				users.remove(index);
				server.getSubServerManager().getConnection(id)
						.setLoggedIn(false);
			}
		} catch (InvalidIdentifierException e) {
			server.getServerIO().printErr(
					"[" + this + "] Exception, user by " + id
							+ " doesn't exist.");
			server.getServerIO().writeException(e);
			return false;
		}
		return true;
	}

	public int getID(int index) throws InvalidIdentifierException {
		int id = -1;
		id = ridl.getId(index);
		if (id < 0) {
			if (users.size() >= index) {
				ridl.addToIDList(users.get(index).ID, index);
				return users.get(index).ID;
			}
		} else {
			return id;
		}
		throw new InvalidIdentifierException("[" + this + "] Exception, "
				+ index + " is not a valid index.");
	}

	public int getIndex(int id) throws InvalidIdentifierException {
		int index = -1;
		index = ridl.getIndex(id);
		if (index < 0) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).ID == id) {
					ridl.addToIDList(id, i);
					return i;
				}
			}
		} else {
			return index;
		}
		throw new InvalidIdentifierException("[" + this + "] Exception, " + id
				+ " is not a valid identifier.");
	}

	public UserProfile getProfile(int id) throws InvalidIdentifierException {
		try {
			return users.get(getIndex(id));
		} catch (InvalidIdentifierException e) {
			throw e;
		}
	}

	public UserProfile getProfile(String name)
			throws InvalidIdentifierException {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).username.equalsIgnoreCase(name)) {
				return users.get(i);
			}
		}
		throw new InvalidIdentifierException("[" + this + "] User by name "
				+ name + " does not exist.");
	}

	public void kill() {
		kill = true;
	}

	public boolean loadProfile(String username, String password,
			int subServerIndex, int userID) {
		username.toLowerCase();
		File userFile = new File("./Users/" + username + ".user");
		if (userFile.exists() && !userFile.isDirectory()) {
			ObjectInputStream userDatabaseReader = null;
			try {
				userDatabaseReader = new ObjectInputStream(new FileInputStream(
						"./Users/" + username + ".user"));
				UserProfile profile = (UserProfile) userDatabaseReader
						.readObject();
				userDatabaseReader.close();
				if (profile.username.equalsIgnoreCase(username)) {
					if (profile.verifyPassword(password)) {
						if (!loggedIn(profile.ID)) {
							try {
								server.getSubServerManager()
										.get(subServerIndex)
										.getConnection(userID)
										.logIn(profile.ID, profile.username);
							} catch (InvalidIdentifierException iie) {
								server.getServerIO()
										.printErr(
												"["
														+ this
														+ "] Fatal exception occurred during login.");
								server.getServerIO().writeException(iie);
								SingleDataPacket packet = new SingleDataPacket();
								packet.request = Request.ERROR_LOGIN;
								packet.data.o = new String(
										"Error occured while logging in. Try again later.");
								packet.data.type = DataType.STRING;
								server.getServerIO().sendPacket(subServerIndex,
										userID, packet);
								return false;
							}
							users.add(profile);
							SingleDataPacket packet = new SingleDataPacket();
							packet.request = Request.SUCCESSFUL_LOGIN;
							packet.data.o = new String("Welcome to " + server
									+ "!");
							packet.data.type = DataType.STRING;
							server.getServerIO().sendPacket(subServerIndex,
									profile.ID, packet);
							server.getServerIO().print(
									"[" + this + "] " + profile.username
											+ " has logged in.");
							return true;
						} else {
							SingleDataPacket packet = new SingleDataPacket();
							packet.request = Request.ERROR_LOGIN;
							packet.data.o = new String(
									"Error: That profile is already logged in.");
							packet.data.type = DataType.STRING;
							server.getServerIO().sendPacket(subServerIndex,
									userID, packet);
							return false;
						}
					} else {
						SingleDataPacket packet = new SingleDataPacket();
						packet.request = Request.ERROR_LOGIN;
						packet.data.o = new String("Incorrect password.");
						packet.data.type = DataType.STRING;
						server.getServerIO().sendPacket(subServerIndex, userID,
								packet);
						return false;
					}
				} else {
					SingleDataPacket packet = new SingleDataPacket();
					packet.request = Request.ERROR_LOGIN;
					packet.data.o = new String(
							"There was an error recognizing your profile. \n Contact staff.");
					packet.data.type = DataType.STRING;
					server.getServerIO().sendPacket(subServerIndex, profile.ID,
							packet);
					return false;
				}
			} catch (ClassNotFoundException | IOException ioe) {
				loadProfile(username, password, subServerIndex, userID, true);
			}
		} else if (!userFile.isDirectory()) {
			server.getServerIO().print(
					"[" + this + "] " + username
							+ " is new. Generating profile.");
			addProfile(username, password);
			try {
				server.getSubServerManager().get(subServerIndex)
						.getConnection(userID)
						.logIn(getProfile(username).ID, username);
			} catch (InvalidIdentifierException iie) {
				server.getServerIO()
						.printErr(
								"["
										+ this
										+ "] Fatal exception occurred during login.");
				server.getServerIO().writeException(iie);
				SingleDataPacket packet = new SingleDataPacket();
				packet.request = Request.ERROR_LOGIN;
				packet.data.o = new String(
						"Error occured while logging in. Try again later.");
				packet.data.type = DataType.STRING;
				server.getServerIO().sendPacket(subServerIndex, userID, packet);
				return false;
			}
			SingleDataPacket packet = new SingleDataPacket();
			packet.request = Request.SUCCESSFUL_LOGIN;
			packet.data.o = new String("Welcome to Incrisys2D!");
			packet.data.type = DataType.STRING;
			try {
				server.getServerIO().sendPacket(subServerIndex,
						getProfile(username).ID, packet);
			} catch (InvalidIdentifierException e1) {
				server.getServerIO()
						.printErr(
								"["
										+ this
										+ "] Error sending successful login packet.");
				server.getServerIO().writeException(e1);
			}
			server.getServerIO().print(
					"[" + this + "] " + username + " has logged in.");
			saveNUID();
			try {
				saveProfile(getProfile(username).ID);
			} catch (InvalidIdentifierException e) {
				server.getServerIO().printErr(
						"[" + this + "] Exception, name " + username
								+ " is invalid, and cannot be saved.");
				server.getServerIO().writeException(e);
			}
			return true;
		}
		return false;
	}

	public boolean loadProfile(String username, String password,
			int subServerIndex, int userID, boolean backup) {
		username.toLowerCase();
		File userFile = new File("./Users/" + username + ".user");
		if (userFile.exists() && !userFile.isDirectory()) {
			ObjectInputStream userDatabaseReader = null;
			try {
				if (backup)
					userDatabaseReader = new ObjectInputStream(
							new FileInputStream("./Users/" + username
									+ "_BACKUP.user"));
				else
					userDatabaseReader = new ObjectInputStream(
							new FileInputStream("./Users/" + username + ".user"));
				UserProfile profile = (UserProfile) userDatabaseReader
						.readObject();
				userDatabaseReader.close();
				if (profile.username.equalsIgnoreCase(username)) {
					if (profile.verifyPassword(password)) {
						if (!loggedIn(profile.ID)) {
							try {
								server.getSubServerManager()
										.get(subServerIndex)
										.getConnection(userID)
										.logIn(profile.ID, profile.username);
							} catch (InvalidIdentifierException iie) {
								server.getServerIO()
										.printErr(
												"["
														+ this
														+ "] Fatal exception occurred during login.");
								server.getServerIO().writeException(iie);
								SingleDataPacket packet = new SingleDataPacket();
								packet.request = Request.ERROR_LOGIN;
								packet.data.o = new String(
										"Error occured while logging in. Try again later.");
								packet.data.type = DataType.STRING;
								server.getServerIO().sendPacket(subServerIndex,
										userID, packet);
								return false;
							}
							users.add(profile);
							SingleDataPacket packet = new SingleDataPacket();
							packet.request = Request.SUCCESSFUL_LOGIN;
							packet.data.o = new String("Welcome to Incrisys2D!");
							packet.data.type = DataType.STRING;
							server.getServerIO().sendPacket(subServerIndex,
									profile.ID, packet);
							server.getServerIO().print(
									"[" + this + "] " + profile.username
											+ " has logged in.");
							return true;
						} else {
							SingleDataPacket packet = new SingleDataPacket();
							packet.request = Request.ERROR_LOGIN;
							packet.data.o = new String(
									"Error: That profile is already logged in.");
							packet.data.type = DataType.STRING;
							server.getServerIO().sendPacket(subServerIndex,
									userID, packet);
							return false;
						}
					} else {
						SingleDataPacket packet = new SingleDataPacket();
						packet.request = Request.ERROR_LOGIN;
						packet.data.o = new String("Incorrect password.");
						packet.data.type = DataType.STRING;
						server.getServerIO().sendPacket(subServerIndex, userID,
								packet);
						return false;
					}
				} else {
					SingleDataPacket packet = new SingleDataPacket();
					packet.request = Request.ERROR_LOGIN;
					packet.data.o = new String(
							"There was an error recognizing your profile. \n Contact staff.");
					packet.data.type = DataType.STRING;
					server.getServerIO().sendPacket(subServerIndex, profile.ID,
							packet);
					return false;
				}
			} catch (ClassNotFoundException | IOException ioe) {
				if (!backup)
					loadProfile(username, password, subServerIndex, userID,
							true);
				else {
					SingleDataPacket packet = new SingleDataPacket();
					packet.request = Request.ERROR_LOGIN;
					packet.data.o = new String(
							"An exception occurred. Retry a few times. \n If that doesn't work, contact staff.");
					packet.data.type = DataType.STRING;
					server.getServerIO().sendPacket(subServerIndex, userID,
							packet);
					server.getServerIO().printErr(
							"[" + this + "] An exception occurred.");
					server.getServerIO().writeException(ioe);
					return false;
				}
			}
		} else if (!userFile.isDirectory()) {
			server.getServerIO().print(
					"[" + this + "] " + username
							+ " is new. Generating profile.");
			addProfile(username, password);
			try {
				server.getSubServerManager().get(subServerIndex)
						.getConnection(userID)
						.logIn(getProfile(username).ID, username);
			} catch (InvalidIdentifierException iie) {
				server.getServerIO()
						.printErr(
								"["
										+ this
										+ "] Fatal exception occurred during login.");
				server.getServerIO().writeException(iie);
				SingleDataPacket packet = new SingleDataPacket();
				packet.request = Request.ERROR_LOGIN;
				packet.data.o = new String(
						"Error occured while logging in. Try again later.");
				packet.data.type = DataType.STRING;
				server.getServerIO().sendPacket(subServerIndex, userID, packet);
				return false;
			}
			SingleDataPacket packet = new SingleDataPacket();
			packet.request = Request.SUCCESSFUL_LOGIN;
			packet.data.o = new String("Welcome to " + server + "!");
			packet.data.type = DataType.STRING;
			try {
				server.getServerIO().sendPacket(subServerIndex,
						getProfile(username).ID, packet);
			} catch (InvalidIdentifierException e1) {
				server.getServerIO()
						.printErr(
								"["
										+ this
										+ "] Error sending successful login packet.");
				server.getServerIO().writeException(e1);
			}
			server.getServerIO().print(
					"[" + this + "] " + username + " has logged in.");
			saveNUID();
			try {
				saveProfile(getProfile(username).ID);
			} catch (InvalidIdentifierException e) {
				server.getServerIO().printErr(
						"[" + this + "] Exception, name " + username
								+ " is invalid, and cannot be saved.");
				server.getServerIO().writeException(e);
			}
			return true;
		}
		return false;
	}

	public boolean loggedIn(int userID) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).ID == userID) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("./nuid.txt"));
			nextUserID = Integer.parseInt(reader.readLine());
			reader.close();
		} catch (IOException e) {
			server.getServerIO().printErr(
					"[" + this + "] IOException while reading NUID. Not good.");
			server.getServerIO().writeException(e);
			nextUserID = 0;
			saveNUID();
		}
		File userFolder = new File("./Users");
		if (!userFolder.exists()) {
			userFolder.mkdir();
			server.getServerIO().print("[" + this + "] Generated user folder.");
		}
		server.getServerIO().print(
				"[" + this + "] UserDatabase up and running.");
		while (!kill) {
			try {
				sleep(60000);
			} catch (InterruptedException e1) {
				server.getServerIO().writeException(e1);
				kill();
				continue;
			}
			server.getServerIO().print("[" + this + "] Beginning autosave...");
			saveAllProfiles();
		}
		server.getServerIO().print(
				"[" + this + "] Saving all profiles before going down...");
		saveAllProfiles();
		saveNUID();
	}

	public void saveAllProfiles() {
		ObjectOutputStream userDatabaseWriter;
		for (int i = 0; i < users.size(); i++) {
			try {
				userDatabaseWriter = new ObjectOutputStream(
						new FileOutputStream("./Users/"
								+ users.get(i).username.toLowerCase() + ".user"));
				userDatabaseWriter.writeObject(users.get(i));
				userDatabaseWriter.close();
				userDatabaseWriter = new ObjectOutputStream(
						new FileOutputStream("./Users/"
								+ users.get(i).username.toLowerCase()
								+ "_BACKUP.user"));
				userDatabaseWriter.writeObject(users.get(i));
				userDatabaseWriter.close();
				server.getServerIO().print(
						"[" + this + "] " + users.get(i).username.toLowerCase()
								+ "'s profile saved successfully.");
			} catch (IOException e) {
				server.getServerIO().printErr(
						"[" + this + "] Error while saving "
								+ users.get(i).username + "'s profile.");
				server.getServerIO().writeException(e);
			}
		}
	}

	public void saveNUID() {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter("./nuid.txt"));
			writer.write("" + nextUserID);
			writer.close();
			server.getServerIO().print(
					"[" + this + "] Saved NUID. Current NUID is " + nextUserID
							+ ".");
		} catch (IOException ioe) {
			server.getServerIO()
					.printErr(
							"["
									+ this
									+ "] Exception while saving NUID. Not a good thing. NUID is at "
									+ nextUserID + ".");
			server.getServerIO().writeException(ioe);
		}
	}

	public boolean saveProfile(int id) {
		if (id == -1)
			return true;
		ObjectOutputStream userDatabaseWriter = null;
		int index;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e1) {
			server.getServerIO().printErr(
					"[" + this + "] Exception, " + id
							+ " is an Invalid Identifier.");
			server.getServerIO().writeException(e1);
			return false;
		}
		try {
			userDatabaseWriter = new ObjectOutputStream(new FileOutputStream(
					"./Users/" + users.get(index).username.toLowerCase()
							+ ".user"));
			userDatabaseWriter.writeObject(users.get(index));
			userDatabaseWriter.close();
			userDatabaseWriter = new ObjectOutputStream(new FileOutputStream(
					"./Users/" + users.get(index).username.toLowerCase()
							+ "_BACKUP.user"));
			userDatabaseWriter.writeObject(users.get(index));
			userDatabaseWriter.close();
			server.getServerIO().print(
					"[" + this + "] " + users.get(index).username
							+ "'s profile saved successfully.");
			return true;
		} catch (IOException e) {
			server.getServerIO().printErr(
					"[" + this + "] Error while saving "
							+ users.get(index).username + "'s profile.");
			server.getServerIO().writeException(e);
			return false;
		}
	}

	@Override
	public String toString() {
		return "UserDatabase";
	}
}