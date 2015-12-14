package com.chatroom.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JTextArea;

import com.chatroom.connection.ConnectionManager;
import com.chatroom.packets.ChatRoomPacketHandler;
import com.dew.packets.ListenerTimeoutException;
import com.dew.packets.Packet;
import com.dew.packets.PacketData;
import com.dew.packets.PacketListener;
import com.dew.packets.Protocall;
import com.dew.packets.Request;
import com.dew.server.SubServerManager;
import com.dew.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 * 
 */

public final class GUI extends javax.swing.JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369446172292412773L;
	@SuppressWarnings("unused")

	private javax.swing.JTextField commandLine;

	private javax.swing.JTextArea consoleArea;

	private javax.swing.JScrollPane jScrollPane1;

	// TODO Document
	/**
	 * 
	 */
	public GUI() {
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
			processText(commandLine.getText());
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
		consoleArea
				.setFont(consoleArea.getFont().deriveFont(15.0f)/* .deriveFont(Font.BOLD) */);

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

		pack();
	}

	// TODO Document
	/**
	 * 
	 * @param command
	 */
	private void processText(String command) {
	    if (command.startsWith("::")) {
		if (command.startsWith("::register") || command.startsWith("::login")) {
		    String[] text = command.split(" ", 3);
		    if (text.length >= 3) {
			try {
			    if (text[1].length() > (int) ChatRoomPacketHandler.getSingleton().getResponse(Packet.buildPacket(Protocall.GENERAL, Request.ATTRIBUTE_GET).addData(new PacketData(DataType.INT, false).setObject(1)), new PacketListener(Request.ATTRIBUTE_GET)).getData(0).getObject()) {
			        consoleArea.append("Username too long. Only 16 characters allowed.");
			    }
			} catch (ListenerTimeoutException e) {
			    e.printStackTrace();
			}
			ChatRoom.getConnectionManager().sendPacket(Packet.buildPacket(Protocall.GENERAL, Request.ATTEMPT_LOGIN).addData(new PacketData(DataType.STRING, false).setObject(text[1])).addData(new PacketData(DataType.STRING, false).setObject(text[2])));
		    }
		}
	    } else {
		ChatRoom.getConnectionManager().sendPacket(Packet.buildPacket(Protocall.CHAT, Request.MESSAGE).addData(new PacketData(DataType.STRING, false).setObject(command)));
	    }
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
