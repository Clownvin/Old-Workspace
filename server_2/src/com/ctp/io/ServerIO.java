package com.ctp.io;

import com.ctp.server.InvalidIdentifierException;
import com.ctp.server.Server;
import com.ctp.packets.Packet;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ServerIO {

	private final Server server;
	private boolean runtimeLog = false;
	private boolean showTime = false;
	private Logger logger = null;

	public ServerIO(final Server server) {
		this.server = server;
		logger = new Logger(server);
	}

	public boolean getShowTime() {
		return showTime;
	}

	public void print(String s) {
		if (showTime) {
			System.out.println("[" + server.getServerClock().getTime() + "]"
					+ s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage(
						"[" + server.getServerClock().getTime() + "]" + s);
		} else {
			System.out.println(s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage(s);
		}
		if (runtimeLog) {
			if (showTime)
				logger.write("[" + server.getServerClock().getTime() + "]" + s);
			else
				logger.write(s);
		}
	}

	public void print(String s, int[] channels) {
		if (showTime) {
			System.out.println("[" + server.getServerClock().getTime() + "]"
					+ s);
			if (server.getShowGUI()) {
				for (int i : channels)
					server.getConsoleManager().addConsoleMessage(
							"[" + server.getServerClock().getTime() + "]" + s,
							i);
				server.getConsoleManager().addConsoleMessage(s,
						ConsoleManager.CHANNEL_ALL);
			}
		} else {
			System.out.println(s);
			if (server.getShowGUI()) {
				for (int i : channels)
					server.getConsoleManager().addConsoleMessage(s, i);
				server.getConsoleManager().addConsoleMessage(s,
						ConsoleManager.CHANNEL_ALL);
			}
		}
		if (runtimeLog) {
			if (showTime)
				logger.write("[" + server.getServerClock().getTime() + "]" + s);
			else
				logger.write(s);
		}
	}

	public void printErr(String s) {
		if (showTime) {
			System.err.println("[" + server.getServerClock().getTime() + "]"
					+ s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage(
						"[" + server.getServerClock().getTime() + "]" + s);
		} else {
			System.err.println(s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage(s);
		}
		if (runtimeLog) {
			if (showTime)
				logger.write("[" + server.getServerClock().getTime() + "]" + s);
			else
				logger.write(s);
		}
	}

	public void printErr(String s, int[] channels) {
		if (showTime) {
			System.err.println("[" + server.getServerClock().getTime() + "]"
					+ s);
			if (server.getShowGUI()) {
				for (int i : channels)
					server.getConsoleManager().addConsoleMessage(
							"[" + server.getServerClock().getTime() + "]" + s,
							i);
				server.getConsoleManager().addConsoleMessage(s,
						ConsoleManager.CHANNEL_ALL);
			}
		} else {
			System.err.println(s);
			if (server.getShowGUI()) {
				for (int i : channels)
					server.getConsoleManager().addConsoleMessage(s, i);
				server.getConsoleManager().addConsoleMessage(s,
						ConsoleManager.CHANNEL_ALL);
			}
		}
		if (runtimeLog) {
			if (showTime)
				logger.write("[" + server.getServerClock().getTime() + "]" + s);
			else
				logger.write(s);
		}
	}
	
	public void printDebug(String s) {
		if (showTime) {
			System.out.println("[" + server.getServerClock().getTime() + "][<DEBUG>]"
					+ s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage(
						"[" + server.getServerClock().getTime() + "][<DEBUG>]" + s);
		} else {
			System.out.println("[<DEBUG>]"+s);
			if (server.getShowGUI())
				server.getConsoleManager().addConsoleMessage("[<DEBUG>]"+s);
		}
		if (runtimeLog) {
			if (showTime)
				logger.write("[" + server.getServerClock().getTime() + "][<DEBUG>]" + s);
			else
				logger.write("[<DEBUG>]"+s);
		}
	}

	public void sendPacket(int serverIndex, int connectionId, Packet packet) {
		try {
			server.getSubServerManager().get(serverIndex)
					.getConnection(connectionId).addOutgoingPacket(packet);
		} catch (InvalidIdentifierException e) {
			printErr("[" + this + "] Exception, ID " + connectionId
					+ " is an invalid identifier.");
			writeException(e);
		}
	}

	public void setRuntimeLog(final boolean state) {
		this.runtimeLog = state;
	}

	public void setShowTime(final boolean state) {
		this.showTime = state;
	}

	@Override
	public String toString() {
		return "ServerIO";
	}

	public void writeException(Exception e) {
		if (server.getDebug()) {
			if (runtimeLog) {
				e.printStackTrace(logger.getPrintWriter());
				logger.flush();
			}
			e.printStackTrace();
		}
	}
}
