package packets;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.hexane.packets.*;

import client.Client;

public class PacketHandler {
	public final Client client;

	public PacketHandler(final Client CLIENT) {
		this.client = CLIENT;
	}

	public void handlePacket(Packet packet) {
		switch (packet.request) {
		case SUCCESSFUL_LOGIN:
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						"./rememberMe.txt"));
				if (!client.user.rememberMe) {
					writer.write("");
					writer.close();
				} else {
					writer.write("" + client.user.usernameBox);
					writer.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			client.user.name = client.user.usernameBox.substring(0, 1)
					.toUpperCase() + client.user.usernameBox.substring(1);
			client.user.loggedIn = true;
			client.GAMEFRAME.repaint();
			break;
		case ERROR_LOGIN:
			client.loginError = (String) ((SingleDataPacket) packet).data.o;
			client.sendMessageFrame(client.loginError, "Login Error",
					JOptionPane.WARNING_MESSAGE);
			System.err.println(client.loginError);
			break;
		case CHAT_MESSAGE:
			String[] a = ((String) ((SingleDataPacket) packet).data.o)
					.split(": ");
			if (a[0].length() > 2) {
				a[0] = a[0].substring(0, 1).toUpperCase()
						+ a[0].substring(1).toLowerCase();
			} else {
				a[0] = a[0].toUpperCase();
			}
			String finished = a[0] + ": " + a[1];
			if (client.chatboxMetrics.stringWidth(finished) > 467) {
				int size = 0;
				String[] s = finished.split("");
				for (int j = 0; j < s.length; j++) {
					if (s[j].length() != 0) {
						size += client.chatboxMetrics.charWidth(s[j].charAt(0));
						if (size > 467) {
							client.user.messages[1000 - (client.user.messageIndex)] = finished
									.substring(0, j);
							client.user.messagePointer = client.user.messageIndex;
							client.user.messageIndex++;
							client.user.messages[1000 - (client.user.messageIndex)] = "     "
									+ finished.substring(j);
							client.user.messagePointer = client.user.messageIndex;
							client.user.messageIndex++;
							j = s.length;
						}
					}
				}
			} else {
				client.user.messages[1000 - (client.user.messageIndex)] = finished;
				client.user.messagePointer = client.user.messageIndex;
				client.user.messageIndex++;
			}
			break;
		default:
			System.err.println("Unknown request: " + packet.request.toString());
			break;

		}
	}
}
