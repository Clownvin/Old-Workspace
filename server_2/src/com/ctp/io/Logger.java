package com.ctp.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ctp.server.Server;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class Logger {

	private final Server server;
	private BufferedWriter bufferedWriter;
	private PrintWriter printWriter;
	private int dayID = 0;

	public Logger(final Server SERVER) {
		this.server = SERVER;
		loadWriters();
	}

	public void close() {
		try {
			bufferedWriter.close();
			printWriter.close();
		} catch (IOException e) {
			server.getServerIO()
					.printErr(
							"["
									+ this
									+ "] IOException occured while closing writers.");
			server.getServerIO().writeException(e);
		}
	}

	public void flush() {
		try {
			bufferedWriter.flush();
		} catch (IOException e) {
			server.getServerIO()
					.printErr(
							"["
									+ this
									+ "] IOException occured while flushing stream.");
			server.getServerIO().writeException(e);
		}
	}

	public BufferedWriter getBufferedWriter() {
		return this.bufferedWriter;
	}

	public PrintWriter getPrintWriter() {
		return this.printWriter;
	}

	public void loadWriters() {
		File userFolder = new File("./ServerLogs");
		if (!userFolder.exists()) {
			userFolder.mkdir();
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Date date = new Date();
		String year = dateFormat.format(date);
		userFolder = new File("./ServerLogs/" + year + " Logs");
		if (!userFolder.exists()) {
			userFolder.mkdir();
		}
		String day = "Null";
		String month = "Null";
		String day2 = "Null";
		Calendar calendar = Calendar.getInstance();
		dayID = calendar.get(Calendar.DAY_OF_WEEK);
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
		userFolder = new File("./ServerLogs/" + year + " Logs/" + month);
		if (!userFolder.exists()) {
			userFolder.mkdir();
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
		try {
			File logFile = new File("./ServerLogs/" + year + " Logs/" + month
					+ "/" + day2 + " of " + month + " "
					+ dateFormat.format(date) + " - " + day + ".log");
			if (!logFile.exists()) {
				bufferedWriter = new BufferedWriter(new FileWriter(logFile,
						true));
				printWriter = new PrintWriter(bufferedWriter);
				dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				bufferedWriter.write("--[Server log for date: "
						+ dateFormat.format(date) + "]--");
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.flush();
			} else {
				bufferedWriter = new BufferedWriter(new FileWriter(logFile,
						true));
				printWriter = new PrintWriter(bufferedWriter);
			}
		} catch (IOException e) {
			server.getServerIO().printErr(
					"[" + this + "] IOException occured while starting up.");
			server.getServerIO().writeException(e);
		}
	}

	@Override
	public String toString() {
		return "Logger";
	}

	public void write(String string) {
		try {
			Calendar calendar = Calendar.getInstance();
			if (calendar.get(Calendar.DAY_OF_WEEK) != dayID) {
				close();
				loadWriters();
			}
			bufferedWriter.write(string);
			bufferedWriter.newLine();
			bufferedWriter.flush();
		} catch (IOException e) {
			server.getServerIO().printErr(
					"[" + this + "] IOException occured while writing data.");
			server.getServerIO().writeException(e);
		}
	}
}
