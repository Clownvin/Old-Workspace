package com.ctp.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import com.ctp.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class FileManager {
	// TODO Document
	/**
	 * 
	 * @param path
	 */
	public static void checkFilePath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public static String getBackupPath() {
		if (dayId == Calendar.getInstance().get(Calendar.DAY_OF_WEEK))
			return backupPath;
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year = dateFormat.format(date);
		String day = "Null";
		String month = "Null";
		String day2 = "Null";
		Calendar calendar = Calendar.getInstance();
		calendar.get(Calendar.DAY_OF_WEEK);
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			day = "Sunday";
			break;
		case 2:
			day = "Monday";
			break;
		case 3:
			day = "Tuesday";
			break;
		case 4:
			day = "Wednesday";
			break;
		case 5:
			day = "Thursday";
			break;
		case 6:
			day = "Friday";
			break;
		case 7:
			day = "Saturday";
			break;
		}
		switch (calendar.get(Calendar.MONTH)) {
		case 0:
			month = "January";
			break;
		case 1:
			month = "February";
			break;
		case 2:
			month = "March";
			break;
		case 3:
			month = "April";
			break;
		case 4:
			month = "May";
			break;
		case 5:
			month = "June";
			break;
		case 6:
			month = "July";
			break;
		case 7:
			month = "August";
			break;
		case 8:
			month = "September";
			break;
		case 9:
			month = "October";
			break;
		case 10:
			month = "November";
			break;
		case 11:
			month = "December";
			break;
		}
		switch (calendar.get(Calendar.DAY_OF_MONTH)) {
		case 1:
		case 21:
		case 31:
			day2 = calendar.get(Calendar.DAY_OF_MONTH) + "st";
			break;
		case 2:
		case 22:
		case 32:
			day2 = calendar.get(Calendar.DAY_OF_MONTH) + "nd";
			break;
		case 3:
		case 23:
			day2 = calendar.get(Calendar.DAY_OF_MONTH) + "rd";
			break;
		default:
			day2 = calendar.get(Calendar.DAY_OF_MONTH) + "th";
			break;
		}
		File backupFolder = new File("./Backups/" + year + " Backups/" + month
				+ "/" + day2 + " of " + month + " " + dateFormat.format(date)
				+ " - " + day);
		if (!backupFolder.exists()) {
			backupFolder.mkdirs();
		}
		backupPath = backupFolder.getPath();
		return backupFolder.getPath();
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public static FileManager getSingleton() {
		return SINGLETON;
	}

	// TODO Document
	/**
	 * 
	 * @param template
	 * @param fileName
	 * @return
	 */
	public static FileManagerWriteable<?> readFile(FileManagerWriteable<?> template,
			String fileName) {
		File file = new File(template.getFileType().getPath() + fileName
				+ template.getFileType().getExtension());
		if (file.exists() && !file.isDirectory()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				ArrayList<String> lines = new ArrayList<String>();
				String line = reader.readLine();
				while (line != null) {
					lines.add(line);
					line = reader.readLine();
				}
				reader.close();
				String[] strings = new String[lines.size()];
				for (int i = 0; i < lines.size(); i++) {
					strings[i] = lines.get(i);
				}
				byte[][] bytes = new byte[strings.length][];
				int stringIndex = -1;
				for (int i = 0; i < bytes.length; i++) {
					stringIndex++;
					if (strings[i] == null || strings[i] == "") {
						bytes = Arrays.copyOfRange(bytes, 0, bytes.length - 1);
						i--;
						continue;
					}
					bytes[i] = BinaryOperations
							.characterArrayToByteArray(strings[stringIndex]
									.toCharArray());
				}
				return template.fromBytes(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return null;
		}
		return null;
	}

	// TODO Document
	/**
	 * 
	 * @param object
	 */
	public static void writeBackupFile(FileManagerWriteable<?> object) {
		try {
			checkFilePath(getBackupPath() + "/"
					+ object.getFileType().getPath().replace("./", ""));
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					getBackupPath() + "/"
							+ object.getFileType().getPath().replace("./", "")
							+ object.getFileName()
							+ object.getFileType().getExtension()));
			for (byte[] a : object.toBytes()) {
				writer.write(BinaryOperations.byteArrayToCharacterArray(a));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// TODO Document
	/**
	 * 
	 * @param object
	 */
	public static void writeFile(FileManagerWriteable<?> object) {
		try {
			checkFilePath(object.getFileType().getPath());
			BufferedWriter writer = new BufferedWriter(new FileWriter(object
					.getFileType().getPath()
					+ object.getFileName()
					+ object.getFileType().getExtension()));
			for (byte[] a : object.toBytes()) {
				writer.write(BinaryOperations.byteArrayToCharacterArray(a));
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static final String RAW_DATA_PATH = "./Data/Raw Data/";

	public static final String RAW_DATA_FILE_EXTENSION = ".dat";

	public static final String LOG_FILE_EXTENSION = ".log";

	public static final String MODULE_PATH = "./Data/Modules/";

	public static final String MODULE_FILE_EXTENSION = ".module";
	
	private static String backupPath = "";
	
	private static int dayId = -1;

	private static final FileManager SINGLETON = new FileManager();

	// TODO Document
	/**
	 * 
	 */
	private FileManager() {
		// Private so can't instantiate
	}
}
