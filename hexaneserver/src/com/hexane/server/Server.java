package com.hexane.server;

import com.hexane.connection.ConnectionAcceptor;
import com.hexane.io.ConsoleManager;
import com.hexane.io.ServerIO;
import com.hexane.users.UserDatabase;
import com.hexane.util.CycleProcessManager;
import com.hexane.util.ServerClock;

/**
 * 
 * @author Calvin Gene Hall
 * 
 */
public final class Server implements Runnable {

	public static void main(String[] args) {
		Server server = new Server();
		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-d")) {
				server.serverIO.print("[" + server + "] Debug Mode.");
				server.debug = true;
			} else if (args[i].equalsIgnoreCase("-r")) {
				server.getServerIO().setRuntimeLog(true);
				server.getServerIO().print("[" + server + "]");
				server.getServerIO().print("[" + server + "]");
				server.getServerIO().print(
						"[" + server + "] Runtime Log Active.");
			} else if (args[i].equalsIgnoreCase("-s")) {
				i++;
				server.subServerManager.setSubServerMaxSize(Integer
						.parseInt(args[i]));
				server.getServerIO().print(
						"[" + server + "] SubServer max connections set to "
								+ server.subServerManager.getSubServerMaxSize()
								+ ".");
			} else if (args[i].equalsIgnoreCase("-lowcpu")) {
				server.lowCPU = true;
				server.getServerIO()
						.print("["
								+ server
								+ "] Throwing in some sleep calls to reduce CPU usage.");
			} else if (args[i].equalsIgnoreCase("-t")) {
				server.getServerIO().setShowTime(true);
				server.getServerIO().print(
						"[" + server + "] Print methods will display time.");
			} else if (args[i].equalsIgnoreCase("-p")) {
				i++;
				server.subServerManager.setPercentToCreateSub(Double
						.parseDouble(args[i]));
				server.getServerIO()
						.print("["
								+ server
								+ "] Will create new SubServer when a server reaches "
								+ (server.subServerManager
										.getPercentToCreateSub() * 100)
								+ "% max population.");
			} else if (args[i].equalsIgnoreCase("-ms")) {
				i++;
				server.subServerManager.setMaxSubs(Integer.parseInt(args[i]));
				server.getServerIO()
						.print("["
								+ server
								+ "] Will stop creating new SubServers when there are "
								+ server.subServerManager.getMaxSubs()
								+ " subs.");
			} else if (args[i].equalsIgnoreCase("-g")) {
				server.showGUI = true;
				server.getConsoleManager().setVisible(true);
				server.getServerIO().print("[" + server + "] Will create GUI.");
			} else if (args[i].equalsIgnoreCase("-help")) {
				System.out.println("Known arguments:");
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
		server.run();
	}

	private final ServerIO serverIO = new ServerIO(this);

	private final ConsoleManager consoleManager = new ConsoleManager(this);
	private final SubServerManager subServerManager = new SubServerManager(this);
	private final CycleProcessManager cycleProcessManager = new CycleProcessManager(
			this);
	private final UserDatabase userDatabase = new UserDatabase(this);
	private final ServerClock serverClock = new ServerClock();
	private final ConnectionAcceptor connectionAcceptor = new ConnectionAcceptor(
			this, 2224);
	private boolean showGUI = false;
	private boolean lowCPU = false;
	private boolean debug = false;
	private volatile boolean kill = false;

	public ConsoleManager getConsoleManager() {
		return consoleManager;
	}

	public CycleProcessManager getCycleProcessManager() {
		return cycleProcessManager;
	}

	public boolean getDebug() {
		return debug;
	}

	public boolean getLowCPU() {
		return lowCPU;
	}

	public ServerClock getServerClock() {
		return serverClock;
	}

	public ServerIO getServerIO() {
		return serverIO;
	}

	public boolean getShowGUI() {
		return showGUI;
	}

	public SubServerManager getSubServerManager() {
		return subServerManager;
	}

	public UserDatabase getUserDatabase() {
		return userDatabase;
	}

	public void kill() {
		kill = true;
	}

	@Override
	public void run() {
		cycleProcessManager.start();
		subServerManager.add(new SubServer(this, 0, subServerManager
				.getSubServerMaxSize()));
		connectionAcceptor.start();
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				serverIO.print("[" + this + "] Shutting everything down.");
				userDatabase.interrupt();
				userDatabase.kill();
				try {
					userDatabase.join();
				} catch (InterruptedException e) {
					serverIO.writeException(e);
				}
				subServerManager.kill();
			}

		}));
		while (!kill) {
			try {
				if (lowCPU)
					Thread.sleep(1);
			} catch (InterruptedException e) {
				serverIO.writeException(e);
			}
		}
		userDatabase.kill();
		subServerManager.kill();
	}

	@Override
	public String toString() {
		return "Hexane";
	}
}
