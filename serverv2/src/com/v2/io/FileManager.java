package com.v2.io;

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

import com.v2.util.BinaryOperations;

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
	File folder = new File(path);
	if (!folder.exists()) {
	    folder.mkdirs();
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
     * @return
     */
    public static <T extends FileManagerWriteable> T readFile(T template,
	    String fileName) {
	if (template == null)
	    throw new NullPointerException(
		    "Template must be a valid, instantiated object of the desired type.");
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
		byte[][] bytes = new byte[lines.size()][];
		for (int i = 0, stringIndex = 0; i < bytes.length; i++, stringIndex++) {
		    if (lines.get(stringIndex) == null
			    || lines.get(stringIndex) == "") {
			bytes = Arrays.copyOfRange(bytes, 0, bytes.length - 1); // Trimming
										// off
										// excess
										// index.
			i--;
			continue;
		    }
		    bytes[i] = BinaryOperations.characterArrayToByteArray(lines
			    .get(stringIndex).toCharArray());
		}
		// Unless you purposely try and throw it a curve ball, nothing
		// should happen.
		// Throwing a curve ball will be difficult, I might add,
		// considering it should
		// always return an object of the same type as the template, but
		// since the method can be overridden, who knows.
		Object o = template.fromBytes(bytes);
		if (o != null) {
		    @SuppressWarnings("unchecked")
		    T result = (T) o; // Only reason I split the return
				      // statement up was so I could throw in
				      // the unchecked annotation.
		    return result;
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    // TODO Document
    /**
     * 
     * @param object
     */
    public static void writeBackupFile(FileManagerWriteable object) {
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
    public static void writeFile(FileManagerWriteable object) {
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
	// So can't be instantiated
    }
}
