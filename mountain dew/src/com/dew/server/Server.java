package com.dew.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.dew.connection.ConnectionAcceptor;
import com.dew.io.BackupTask;
import com.dew.io.ConsoleManager;
import com.dew.io.FileManager;
import com.dew.io.ServerIO;
import com.dew.modules.ModuleManager;
import com.dew.packets.Packet;
import com.dew.packets.PacketHandler;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.threading.ThreadPool;
import com.dew.users.UserDatabase;
import com.dew.util.CycleProcessManager;
import com.dew.util.ServerClock;
import com.dew.util.Utilities;

/**
 * 
 * @author Calvin Gene Hall
 * 
 */
public final class Server extends Thread implements Runnable {

	public static ConnectionAcceptor getConnectionAcceptor() {
		return CONNECTION_ACCEPTOR;
	}

	public static ConsoleManager getConsoleManager() {
		return CONSOLE_MANAGER;
	}

	public static CycleProcessManager getCycleProcessManager() {
		return CYCLE_PROCESS_MANAGER;
	}

	public static boolean getDebug() {
		return debug;
	}

	public static FileManager getFileManager() {
		return FILE_MANAGER;
	}

	public static boolean getLowCPU() {
		return lowCPU;
	}

	public static ModuleManager getModuleManager() {
		return MODULE_MANAGER;
	}

	public static PacketHandler getPacketHandler() {
		return PACKET_HANDLER;
	}

	public static Server getServer() {
		return SERVER;
	}

	public static ServerClock getServerClock() {
		return SERVER_CLOCK;
	}

	public static ServerIO getServerIo() {
		return SERVER_IO;
	}

	public static boolean getShowGUI() {
		return showGUI;
	}

	public static SubServerManager getSubServerManager() {
		return SUB_SERVER_MANAGER;
	}

	public static UserDatabase getUserDatabase() {
		return USER_DATABASE;
	}

	public static boolean isShuttingDown() {
		return shuttingDown;
	}

	public static void kill() {
		kill = true;
		synchronized (SERVER) {
			SERVER.notifyAll();
		}
	}

	public static void main(String[] args) {
		boolean d = false, r = false, l = false, s = false;
		int m = -1;
		int ms = -1;
		int port = 2224;
		double p = -1.0D;
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-d")) {
				d = true;
			} else if (args[i].equalsIgnoreCase("-r")) {
				r = true;
			} else if (args[i].equalsIgnoreCase("-s")) {
				i++;
				m = Integer.parseInt(args[i]);
			} else if (args[i].equalsIgnoreCase("-lowcpu")) {
				l = true;
			} else if (args[i].equalsIgnoreCase("-t")) {
				ServerIO.setShowTime(true);
			} else if (args[i].equalsIgnoreCase("-p")) {
				i++;
				p = Double.parseDouble(args[i]);
			} else if (args[i].equalsIgnoreCase("-port")) {
				i++;
				port = Integer.parseInt(args[i]);
			} else if (args[i].equalsIgnoreCase("-ms")) {
				i++;
				ms = Integer.parseInt(args[i]);
			} else if (args[i].equalsIgnoreCase("-g")) {
				s = true;
			} else if (args[i].equalsIgnoreCase("-help")) {
				System.out.println("Known arguments:");
				System.out
						.println("-port #       -  Set the port that the ConnectionAcceptor uses.");
				System.out
						.println("-lowcpu       -  Server less CPU consumptive.");
				System.out
						.println("-s #          -  Set SubServer connection limit. Will make more SubServers if limit reached.");
				System.out
						.println("-r            -  Runtime log logs server activity in external text file serverlog.log");
				System.out
						.println("-d            -  Debug Mode. Server outputs exception traces instead of hiding them.");
				System.out
						.println("-t            -  Set the Server's print methods to display the time when print is called.");
				System.out
						.println("-p #.#        -  Set the % max population at which server creates a new sub to share load.");
				System.out
						.println("-ms #         -  Set the max number of sub servers.");
				System.out.println("-help         -  Displays this menu.");
				return;
			} else {
				System.out.println("Unknown argument '" + args[i]
						+ "'. Known arguments:");
				System.out
						.println("-port #       -  Set the port that the ConnectionAcceptor uses.");
				System.out
						.println("-lowcpu       -  Server less CPU consumptive.");
				System.out
						.println("-s #          -  Set SubServer connection limit. Will make more SubServers if limit reached.");
				System.out
						.println("-r            -  Runtime log logs server activity in external text file serverlog.log");
				System.out
						.println("-d            -  Debug Mode. Server outputs exception traces instead of hiding them.");
				System.out
						.println("-t            -  Set the Server's print methods to display the time when print is called.");
				System.out
						.println("-p #.#        -  Set the % max population at which server creates a new sub to share load.");
				System.out
						.println("-ms #         -  Set the max number of sub servers.");
				System.out.println("-help         -  Displays this menu.");
				return;
			}
		}
		@SuppressWarnings("unused")
		Server server = new Server(d, r, l, s, m, ms, p, port);
	}

	public static void setShuttingDown(boolean shuttingDown) {
		Server.shuttingDown = shuttingDown;
	}

	public static void shutDown() {
		if (!isShuttingDown()) {
			ServerIO.print("[Server] Shutting everything down.");
			setShuttingDown(true);
			CycleProcessManager.kill();
			ConnectionAcceptor.kill();
			getConnectionAcceptor().interrupt();
			ServerClock.kill();
			UserDatabase.kill();
			SubServerManager.kill();
			Server.kill();
		}
	}

	private static ConnectionAcceptor CONNECTION_ACCEPTOR;

	private static boolean showGUI = false;

	private static boolean lowCPU = false;

	private static boolean debug = false;

	private static volatile boolean kill = false;

	private static volatile boolean shuttingDown = false;

	private final static UserDatabase USER_DATABASE = UserDatabase
			.getSingleton();

	private final static ConsoleManager CONSOLE_MANAGER = ConsoleManager
			.getSingleton();

	private final static FileManager FILE_MANAGER = FileManager.getSingleton();

	private final static ServerIO SERVER_IO = ServerIO.getSingleton();

	private final static PacketHandler PACKET_HANDLER = PacketHandler
			.getSingleton();

	private final static SubServerManager SUB_SERVER_MANAGER = SubServerManager
			.getSingleton();

	private final static CycleProcessManager CYCLE_PROCESS_MANAGER = CycleProcessManager
			.getSingleton();

	private final static ServerClock SERVER_CLOCK = ServerClock.getSingleton();

	private final static ModuleManager MODULE_MANAGER = ModuleManager
			.getSingleton();

	private static Server SERVER = null;

	private Server(boolean d, boolean r, boolean l, boolean s, int m, int ms,
			double p, int port) {
		SERVER = this;
//		DecimalFormat s3 = new DecimalFormat("#");
//		for (int i = 1; i < 451; i++) {
//			System.out.println(i +" : "+s3.format(Math.pow((i * 100), 2) / 50));
//		}
		CONNECTION_ACCEPTOR = new ConnectionAcceptor(port);
		Server.debug = d;
		ServerIO.setRuntimeLog(r);
		Server.lowCPU = l;
		ConsoleManager.setVisible(true);
		Server.showGUI = s;
		ServerIO.print("[" + this + "] Server is starting up...");
		if (d)
			ServerIO.print("[" + this + "] Server is in DEBUG mode.");
		if (r)
			ServerIO.print("[" + this + "] Runtime log is active.");
		if (s)
			ServerIO.print("[" + this + "] Will display console.");
		if (l)
			ServerIO.print("[" + this
					+ "] Reducing CPU through 1ms sleep calls.");
		if (m != -1) {
			SubServerManager.setSubServerMaxSize(m);
			ServerIO.print("[" + this + "] SubServer max connections set to "
					+ m + ".");
		}
		if (ms != -1) {
			SubServerManager.setMaxSubs(ms);
			ServerIO.print("[" + this + "] Max SubServers set to " + ms + ".");
		}
		if (p != -1.0D) {
			SubServerManager.setPercentToCreateSub(p);
			ServerIO.print("["
					+ this
					+ "] New SubServers will be created when all SubServers are "
					+ p * 100 + "% full.");
		}
		this.start();
	}

	@Override
	public void run() {
		ThreadPool.addTask(new BackupTask());
		ModuleManager.loadModules();
		SubServerManager.add(new SubServer(0, SubServerManager
				.getSubServerMaxSize()));
		CONNECTION_ACCEPTOR.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				shutDown();
			}

		}));
		while (!kill) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			ServerIO.printDebug("[Server] Exited wait block.");
		}
		if (!isShuttingDown())
			shutDown();
	}

	@Override
	public String toString() {
		return "MountainDew";
	}
}
