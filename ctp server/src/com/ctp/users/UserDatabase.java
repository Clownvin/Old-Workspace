package com.ctp.users;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.ctp.server.InvalidIdentifierException;
import com.ctp.server.SubServerManager;
import com.ctp.io.FileManager;
import com.ctp.io.FileType;
import com.ctp.io.ServerIO;
import com.ctp.packets.PacketData;
import com.ctp.packets.Request;
import com.ctp.packets.Packet;
import com.ctp.util.BinaryOperations;
import com.ctp.util.DataType;
import com.ctp.util.RecentIDList;
import com.ctp.util.ThreadPool;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class UserDatabase extends Thread implements Runnable {
	public static void addProfile(String username, String password) {
		UserProfile newUser = new UserProfile(nextUserID);
		nextUserID++;
		newUser.username = username;
		newUser.setPassword(password);
		USERS.add(newUser);
		ServerIO.print("[UserDatabase] Added new user " + username
				+ " with ID " + (nextUserID - 1) + ".");
	}

	public static boolean closeProfile(int id) {
		if (id == -1)
			return true;
		int index = -1;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e) {
			ServerIO.printErr("[UserDatabase] Exception, user by ID " + id
					+ " does not exist.");
			ServerIO.writeException(e);
			return false;
		}
		try {
			if (SubServerManager.getConnection(id).isLoggedIn()) {
				saveProfile(id);
				ServerIO.print("[UserDatabase] " + USERS.get(index).username
						+ "'s profile closed successfully.");
				USERS.remove(index);
				SubServerManager.getConnection(id).setLoggedIn(false);
			}
		} catch (InvalidIdentifierException e) {
			ServerIO.printErr("[UserDatabase] Exception, user by " + id
					+ " doesn't exist.");
			ServerIO.writeException(e);
			return false;
		}
		return true;
	}

	public static int getID(int index) throws InvalidIdentifierException {
		int id = -1;
		id = ridl.getId(index);
		if (id < 0) {
			if (USERS.size() >= index) {
				ridl.addToIDList(USERS.get(index).ID, index);
				return USERS.get(index).ID;
			}
		} else {
			return id;
		}
		throw new InvalidIdentifierException("[UserDatabase] Exception, "
				+ index + " is not a valid index.");
	}

	public static int getIndex(int id) throws InvalidIdentifierException {
		int index = -1;
		index = ridl.getIndex(id);
		if (index < 0) {
			for (int i = 0; i < USERS.size(); i++) {
				if (USERS.get(i).ID == id) {
					ridl.addToIDList(id, i);
					return i;
				}
			}
		} else {
			return index;
		}
		throw new InvalidIdentifierException("[UserDatabase] Exception, " + id
				+ " is not a valid identifier.");
	}

	public static UserProfile getProfile(int id)
			throws InvalidIdentifierException {
		try {
			return USERS.get(getIndex(id));
		} catch (InvalidIdentifierException e) {
			throw e;
		}
	}

	public static UserProfile getProfile(String name)
			throws InvalidIdentifierException {
		for (int i = 0; i < USERS.size(); i++) {
			if (USERS.get(i).username.equalsIgnoreCase(name)) {
				return USERS.get(i);
			}
		}
		throw new InvalidIdentifierException("[UserDatabase] User by name "
				+ name + " does not exist.");
	}

	public static UserDatabase getSingleton() {
		return SINGLETON;
	}

	public static void kill() {
		kill = true;
	}

	public static boolean loadProfile(String username, String password,
			int subServerIndex, int userID) {
		username.toLowerCase();
		File userFile = new File(FileType.USER.getPath() + username
				+ FileType.USER.getExtension());
		if (userFile.exists() && !userFile.isDirectory()) {
			UserProfile profile = (UserProfile) FileManager.readFile(
					new UserProfile(-1), username);
			if (profile.username.equalsIgnoreCase(username)) {
				if (profile.verifyPassword(password)) {
					if (!loggedIn(profile.ID)) {
						try {
							SubServerManager.get(subServerIndex)
									.getConnection(userID)
									.logIn(profile.ID, profile.username);
						} catch (InvalidIdentifierException iie) {
							ServerIO.printErr("[UserDatabase] Fatal exception occurred during login.");
							ServerIO.writeException(iie);
							Packet packet = Packet.buildPacket(
									new PacketData(DataType.STRING, false))
									.setRequest(Request.ERROR_LOGIN);
							packet.getData(0)
									.setObject(
											new String(
													"Error occured while logging in. Try again later."))
									.setDataType(DataType.STRING);
							ServerIO.sendPacket(subServerIndex, userID, packet);
							return false;
						}
						USERS.add(profile);
						Packet packet = Packet.buildPacket((
								new PacketData(DataType.STRING, false)));
						packet.setRequest(Request.SUCCESSFUL_LOGIN);
						packet.getData(0)
								.setObject(
										new String(
												"Welcome to Calvin's Server!"))
								.setDataType(DataType.STRING);
						ServerIO.sendPacket(subServerIndex, profile.ID, packet);
						ServerIO.print("[UserDatabase] " + profile.username
								+ " has logged in.");
						return true;
					} else {
						Packet packet = Packet.buildPacket((
								new PacketData(DataType.STRING, false)));
						packet.setRequest(Request.ERROR_LOGIN);
						packet.getData(0)
								.setObject(
										new String(
												"Error: That profile is already logged in."))
								.setDataType(DataType.STRING);
						ServerIO.sendPacket(subServerIndex, userID, packet);
						return false;
					}
				} else {
					Packet packet = Packet.buildPacket(
							new PacketData(DataType.STRING, false));
					packet.setRequest(Request.ERROR_LOGIN);
					packet.getData(0)
							.setObject(new String("Incorrect password."))
							.setDataType(DataType.STRING);
					ServerIO.sendPacket(subServerIndex, userID, packet);
					return false;
				}
			} else {
				Packet packet = Packet.buildPacket(new PacketData(
						DataType.STRING, false));
				packet.setRequest(Request.ERROR_LOGIN);
				packet.getData(0)
						.setObject(
								new String(
										"There was an error recognizing your account. \n If problem persists, contact support."))
						.setDataType(DataType.STRING);
				ServerIO.sendPacket(subServerIndex, profile.ID, packet);
				return false;
			}
		} else if (!userFile.isDirectory()) {
			ServerIO.print("[UserDatabase] " + username
					+ " is new. Generating profile.");
			addProfile(username, password);
			try {
				SubServerManager.get(subServerIndex).getConnection(userID)
						.logIn(getProfile(username).ID, username);
			} catch (InvalidIdentifierException iie) {
				ServerIO.printErr("[UserDatabase] Fatal exception occurred during login.");
				ServerIO.writeException(iie);
				Packet packet = Packet.buildPacket(new PacketData(
						DataType.STRING, false));
				packet.setRequest(Request.ERROR_LOGIN);
				packet.getData(0)
						.setObject(
								new String(
										"Error occured while loggin in. Try again later."))
						.setDataType(DataType.STRING);
				ServerIO.sendPacket(subServerIndex, userID, packet);
				return false;
			}
			Packet packet = Packet.buildPacket(new PacketData(
					DataType.STRING, false));
			packet.setRequest(Request.SUCCESSFUL_LOGIN);
			packet.getData(0)
					.setObject(new String("Welcome to Calvin's Server"))
					.setDataType(DataType.STRING);
			try {
				ServerIO.sendPacket(subServerIndex, getProfile(username).ID,
						packet);
			} catch (InvalidIdentifierException e1) {
				ServerIO.printErr("[UserDatabase] Error sending successful login packet.");
				ServerIO.writeException(e1);
			}
			ServerIO.print("[UserDatabase] " + username + " has logged in.");
			saveNUID();
			try {
				saveProfile(getProfile(username).ID);
			} catch (InvalidIdentifierException e) {
				ServerIO.printErr("[UserDatabase] Exception, name " + username
						+ " is invalid, and cannot be saved.");
				ServerIO.writeException(e);
			}
			return true;
		}
		return false;
	}

	public static boolean loadProfile(String username, String password,
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
								SubServerManager.get(subServerIndex)
										.getConnection(userID)
										.logIn(profile.ID, profile.username);
							} catch (InvalidIdentifierException iie) {
								ServerIO.printErr("[UserDatabase] Fatal exception occurred during login.");
								ServerIO.writeException(iie);
								Packet packet = Packet.buildPacket(
										new PacketData(DataType.STRING, false));
								packet.setRequest(Request.ERROR_LOGIN);
								packet.getData(0)
										.setObject(
												new String(
														"Error occured while logging in. Try again later."))
										.setDataType(DataType.STRING);
								ServerIO.sendPacket(subServerIndex, userID,
										packet);
								return false;
							}
							USERS.add(profile);
							Packet packet = Packet.buildPacket(
									new PacketData(DataType.STRING, false));
							packet.setRequest(Request.SUCCESSFUL_LOGIN);
							packet.getData(0)
									.setObject(
											new String(
													"Welcome to Calvin's Server"))
									.setDataType(DataType.STRING);
							ServerIO.sendPacket(subServerIndex, profile.ID,
									packet);
							ServerIO.print("[UserDatabase] " + profile.username
									+ " has logged in.");
							return true;
						} else {
							Packet packet = Packet.buildPacket(
									new PacketData(DataType.STRING, false));
							packet.setRequest(Request.ERROR_LOGIN);
							packet.getData(0)
									.setObject(
											new String(
													"Error: That profile is already logged in."))
									.setDataType(DataType.STRING);
							ServerIO.sendPacket(subServerIndex, userID, packet);
							return false;
						}
					} else {
						Packet packet = Packet.buildPacket(
								new PacketData(DataType.STRING, false));
						packet.setRequest(Request.ERROR_LOGIN);
						packet.getData(0)
								.setObject(new String("Incorrect password."))
								.setDataType(DataType.STRING);
						ServerIO.sendPacket(subServerIndex, userID, packet);
						return false;
					}
				} else {
					Packet packet = Packet.buildPacket(
							new PacketData(DataType.STRING, false));
					packet.setRequest(Request.ERROR_LOGIN);
					packet.getData(0)
							.setObject(
									new String(
											"There was a problem recognizing your profile. \n If problem persists, please contact support."))
							.setDataType(DataType.STRING);
					ServerIO.sendPacket(subServerIndex, profile.ID, packet);
					return false;
				}
			} catch (ClassNotFoundException | IOException ioe) {
				if (!backup)
					loadProfile(username, password, subServerIndex, userID,
							true);
				else {
					Packet packet = Packet.buildPacket(
							new PacketData(DataType.STRING, false));
					packet.setRequest(Request.ERROR_LOGIN);
					packet.getData(0)
							.setObject(
									new String(
											"An exception occurred. Retry a few times. \n If that doesn't work, contact staff."))
							.setDataType(DataType.STRING);
					ServerIO.sendPacket(subServerIndex, userID, packet);
					ServerIO.printErr("[UserDatabase] An exception occurred.");
					ServerIO.writeException(ioe);
					return false;
				}
			}
		} else if (!userFile.isDirectory()) {
			ServerIO.print("[UserDatabase] " + username
					+ " is new. Generating profile.");
			addProfile(username, password);
			try {
				SubServerManager.get(subServerIndex).getConnection(userID)
						.logIn(getProfile(username).ID, username);
			} catch (InvalidIdentifierException iie) {
				ServerIO.printErr("[UserDatabase] Fatal exception occurred during login.");
				ServerIO.writeException(iie);
				Packet packet = Packet.buildPacket(new PacketData(
						DataType.STRING, false));
				packet.setRequest(Request.ERROR_LOGIN);
				packet.getData(0)
						.setObject(
								new String(
										"Error occured while logging in. Try again later."))
						.setDataType(DataType.STRING);
				ServerIO.sendPacket(subServerIndex, userID, packet);
				return false;
			}
			Packet packet = Packet.buildPacket(new PacketData(
					DataType.STRING, false));
			packet.setRequest(Request.SUCCESSFUL_LOGIN);
			packet.getData(0)
					.setObject(new String("Welcome to Calvin's Server"))
					.setDataType(DataType.STRING);
			try {
				ServerIO.sendPacket(subServerIndex, getProfile(username).ID,
						packet);
			} catch (InvalidIdentifierException e1) {
				ServerIO.printErr("[UserDatabase] Error sending successful login packet.");
				ServerIO.writeException(e1);
			}
			ServerIO.print("[UserDatabase] " + username + " has logged in.");
			saveNUID();
			try {
				saveProfile(getProfile(username).ID);
			} catch (InvalidIdentifierException e) {
				ServerIO.printErr("[UserDatabase] Exception, name " + username
						+ " is invalid, and cannot be saved.");
				ServerIO.writeException(e);
			}
			return true;
		}
		return false;
	}

	public static boolean loggedIn(int userID) {
		for (int i = 0; i < USERS.size(); i++) {
			if (USERS.get(i).ID == userID) {
				return true;
			}
		}
		return false;
	}

	public static void saveAllProfiles() {
		for (int i = 0; i < USERS.size(); i++) {
			FileManager.writeFile(USERS.get(i));
			FileManager.writeBackupFile(USERS.get(i));
		}
		ServerIO.print("[UserDatabase] Saved all profiles.");
	}

	public static void saveNUID() {
		FileManager.checkFilePath(FileManager.RAW_DATA_PATH);
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(FileManager.RAW_DATA_PATH
					+ "nuid" + FileManager.RAW_DATA_FILE_EXTENSION));
			writer.write(""
					+ new String(BinaryOperations
							.byteArrayToCharacterArray(BinaryOperations
									.toBytes(nextUserID))));
			writer.close();
			ServerIO.print("[UserDatabase] Saved NUID. Current NUID is "
					+ nextUserID + ".");
		} catch (IOException ioe) {
			ServerIO.printErr("[UserDatabase] Exception while saving NUID. Not a good thing. NUID is at "
					+ nextUserID + ".");
			ServerIO.writeException(ioe);
		}
	}

	public static boolean saveProfile(int id) {
		if (id == -1)
			return true;
		int index;
		try {
			index = getIndex(id);
		} catch (InvalidIdentifierException e1) {
			ServerIO.printErr("[UserDatabase] Exception, " + id
					+ " is an Invalid Identifier.");
			ServerIO.writeException(e1);
			return false;
		}
		FileManager.writeFile(USERS.get(index));
		FileManager.writeBackupFile(USERS.get(index));
		return true;
	}

	private static volatile boolean kill = false;

	private static int nextUserID = 0;

	private static final ArrayList<UserProfile> USERS = new ArrayList<UserProfile>();

	private static RecentIDList ridl = new RecentIDList(100, 75);

	private static final UserDatabase SINGLETON = new UserDatabase();

	static {
		ThreadPool.start(SINGLETON);
	}

	private UserDatabase() {
		// To prevent instantiation.
	}

	@Override
	public void run() {
		BufferedReader reader;
		try {
			FileManager.checkFilePath(FileManager.RAW_DATA_PATH);
			reader = new BufferedReader(new FileReader(FileManager.RAW_DATA_PATH
					+ "nuid" + FileManager.RAW_DATA_FILE_EXTENSION));
			nextUserID = BinaryOperations
					.bytesToInteger(BinaryOperations
							.characterArrayToByteArray(reader.readLine()
									.toCharArray()));
			reader.close();
		} catch (IOException e) {
			ServerIO.printErr("[UserDatabase] IOException while reading NUID. Not good.");
			ServerIO.writeException(e);
			nextUserID = 0;
			saveNUID();
		}
		ServerIO.print("[UserDatabase] UserDatabase up and running.");
		while (!kill) {
			try {
				sleep(600000); // 10 Minutes.
			} catch (InterruptedException e1) {
				ServerIO.print("[UserDatabase] Interrupted. This is OKAY (in most cases).");
				// ServerIO.writeException(e1);
				kill();
				continue;
			}
			ServerIO.print("[UserDatabase] Beginning autosave...");
			saveAllProfiles();
		}
		ServerIO.print("[UserDatabase] Saving all profiles before going down...");
		saveAllProfiles();
		saveNUID();
	}

	@Override
	public String toString() {
		return "UserDatabase";
	}
}