package com.dew.io;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextArea;

import com.dew.modules.ModuleManager;
import com.dew.server.SubServerManager;

/**
 * 
 * @author Calvin Gene Hall
 * 
 */

public final class Console extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369446172292412773L;
	@SuppressWarnings("unused")
	private ConsoleChannel currentChannel;

	private javax.swing.JTextField commandLine;

	private javax.swing.JTextArea consoleArea;

	private javax.swing.JScrollPane jScrollPane1;

	// TODO Document
	/**
	 * 
	 */
	public Console() {
		initComponents();
	}

	// TODO Document
	/**
	 * 
	 * @param evt
	 */
	private void commandLineKeyPressed(java.awt.event.KeyEvent evt) {
		switch (evt.getKeyCode()) {
		case KeyEvent.VK_ENTER:
			processCommand(commandLine.getText());
			commandLine.setText("");
			break;
		}
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public JTextArea getConsoleArea() {
		return consoleArea;
	}

	// TODO Document
	/**
	 * 
	 */
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		consoleArea = new javax.swing.JTextArea();
		commandLine = new javax.swing.JTextField();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		consoleArea.setBackground(new java.awt.Color(0, 0, 0));
		consoleArea.setColumns(20);
		consoleArea.setForeground(new java.awt.Color(255, 255, 255));
		// consoleArea.setLineWrap(true);
		consoleArea.setRows(5);
		consoleArea.setEditable(false);
		jScrollPane1.setViewportView(consoleArea);
		Font font = null;
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File(
					"./Font/FreeMono.ttf"));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // new Font("./Font/Terminal.ttf", 0, 36);
		System.out.println("Font: " + font);
		consoleArea
				.setFont(font.deriveFont(15.0f)/* .deriveFont(Font.BOLD) */);

		commandLine.setBackground(new java.awt.Color(0, 0, 0));
		commandLine.setForeground(new java.awt.Color(255, 255, 255));

		commandLine.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				commandLineKeyPressed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane1,
						javax.swing.GroupLayout.DEFAULT_SIZE, 400,
						Short.MAX_VALUE).addComponent(commandLine));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup()
						.addComponent(jScrollPane1,
								javax.swing.GroupLayout.DEFAULT_SIZE, 274,
								Short.MAX_VALUE)
						.addComponent(commandLine,
								javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		this.setAlwaysOnTop(true);

		pack();
	}

	// TODO Document
	/**
	 * 
	 * @param command
	 */
	private void processCommand(String command) {
		consoleArea.append("[" + this + "] " + command + "\n");
		if (command.replace(" ", "").toLowerCase().startsWith("commands")) {
			ServerIO.print("[" + this + "] ---------- Commands ---------");
			ServerIO.print("[" + this
					+ "] commands                        -Shows this message.");
			ServerIO.print("["
					+ this
					+ "] color <0-255> <0-255> <0-255>   -Changes the foreground color.");
			ServerIO.print("["
					+ this
					+ "] clear OR cls                    -Clear the console of messages.");
			ServerIO.print("["
					+ this
					+ "] threads                         -Lists currently active threads.");
			ServerIO.print("["
					+ this
					+ "] threadcount                     -Lists the amount of threads currently active in ThreadPool.");
			ServerIO.print("["
					+ this
					+ "] modules                         -Lists currently active modules, and their descriptions.");
		}
		if (command.replace(" ", "").toLowerCase().equalsIgnoreCase("modules")) {
			ModuleManager.listModules();
		}
		if (command.replace(" ", "").toLowerCase()
				.equalsIgnoreCase("subservers")) {
			SubServerManager.listSubServers();
		}
		if (command.replace(" ", "").toLowerCase().startsWith("listmethods")) {
			String[] s = command.trim().split(" ");
			if (s.length > 1)
				ModuleManager.listModuleMethods(s[1]);
		}
		if (command.replace(" ", "").toLowerCase().startsWith("clear")
				|| command.replace(" ", "").toLowerCase().startsWith("cls")) {
			consoleArea.setText("");
		}
		if (command.replace(" ", "").toLowerCase().startsWith("color")) {
			int r, g, b;
			String[] split = command.split(" ");
			try {
				r = Integer.parseInt(split[1]);
				g = Integer.parseInt(split[2]);
				b = Integer.parseInt(split[3]);
			} catch (NumberFormatException e) {
				try {
					consoleArea.append("[" + this + "] \"" + split[1] + " "
							+ split[2] + " " + split[3]
							+ "\" is not a valid RGB color set.\n");
					consoleArea
							.append("["
									+ this
									+ "] Color values MUST be a valid number between 0 and 255.\n");
					return;
				} catch (ArrayIndexOutOfBoundsException aiobe) {
					consoleArea.append("[" + this + "] \""
							+ command.replace("color ", "")
							+ "\" is not a valid RGB color set.\n");
					consoleArea
							.append("["
									+ this
									+ "] The RGB color set must follow the format \"color <0-255> <0-255> <0-255>\" and MUST be a valid number between 0 and 255.\n");
					return;
				}
			} catch (ArrayIndexOutOfBoundsException aiobe) {
				consoleArea.append("[" + this + "] \""
						+ command.replace("color ", "")
						+ "\" is not a valid RGB color set.\n");
				consoleArea
						.append("["
								+ this
								+ "] The RGB color set must follow the format \"color <0-255> <0-255> <0-255>\".\n");
				return;
			}
			if ((r > 255 || r < 0) || (g > 255 || g < 0) || (b > 255 || b < 0)) {
				consoleArea.append("[" + this + "] \"" + split[1] + " "
						+ split[2] + " " + split[3]
						+ "\" is not a valid RGB color set.\n");
				consoleArea
						.append("["
								+ this
								+ "] Color values MUST be a valid number between 0 and 255.\n");
				return;
			}
			consoleArea.setForeground(new Color(r, g, b));
			consoleArea.setBackground(new Color((255 - r) / 10, (255 - g) / 10,
					(255 - b) / 10));
			commandLine.setForeground(new Color(r, g, b));
			commandLine.setBackground(new Color((255 - r) / 10, (255 - g) / 10,
					(255 - b) / 10));
		}
	}

	// TODO Document
	/**
	 * 
	 * @param currentChannelj
	 *            ]; cumulativeData += (bytes[j] & 0xFF); } catch
	 *            (ArrayIndexOutOfBoundsException e) { e.printStackTrace();
	 *            return null;
	 */
	public void setCurrentChannel(ConsoleChannel currentChannel) {
		this.currentChannel = currentChannel;
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "Console";
	}
}
