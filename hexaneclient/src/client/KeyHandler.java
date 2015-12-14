package client;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.hexane.packets.Request;
import com.hexane.packets.SingleDataPacket;

public final class KeyHandler implements KeyListener {
	public final Client CLIENT;
	private boolean alreadyRemovedPass = false;

	public KeyHandler(final Client CLIENT) {
		this.CLIENT = CLIENT;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {

	}

	@Override
	public void keyReleased(KeyEvent arg0) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		switch (arg0.getKeyChar()) {
		case KeyEvent.VK_BACK_SPACE:
			if (!CLIENT.user.loggedIn) {
				if (CLIENT.box1Active) {
					if (CLIENT.user.usernameBox.length() > 0) {
						CLIENT.user.usernameBox = CLIENT.user.usernameBox
								.substring(0,
										CLIENT.user.usernameBox.length() - 1);
					}
				} else if (CLIENT.box2Active) {
					if (CLIENT.user.password.length() > 0) {
						CLIENT.user.password = CLIENT.user.password.substring(
								0, CLIENT.user.password.length() - 1);
						CLIENT.user.passwordBox = CLIENT.user.passwordBox
								.substring(0,
										CLIENT.user.passwordBox.length() - 1);
					}
				}
			} else if (CLIENT.user.typing) {
				if (CLIENT.user.message.length() > 0) {
					CLIENT.user.message = CLIENT.user.message.substring(0,
							CLIENT.user.message.length() - 1);
				}
			}
			break;
		case KeyEvent.VK_ENTER:
			if (!CLIENT.user.loggedIn) {
				if (CLIENT.box1Active) {
					CLIENT.box2Active = true;
					CLIENT.box1Active = false;
					if (CLIENT.user.passwordBox.equalsIgnoreCase("password")
							&& !alreadyRemovedPass) {
						CLIENT.user.passwordBox = "";
						alreadyRemovedPass = true;
					}
				} else if (CLIENT.box2Active) {
					CLIENT.box2Active = false;
					CLIENT.box1Active = false;
					CLIENT.login();
				}
			} else {
				if (CLIENT.user.typing) {
					if (CLIENT.user.message.replaceAll(" ", "").length() > 0) {
						SingleDataPacket packet = new SingleDataPacket();
						packet.request = Request.CHAT_MESSAGE;
						packet.data.o = CLIENT.user.message;
						CLIENT.sendPacket(packet);
						CLIENT.user.message = "";
						CLIENT.user.typing = false;
					} else {
						return;
					}
				} else if (true) {
					CLIENT.user.typing = true;
				}
			}
			break;
		case KeyEvent.VK_SPACE: // 29
			if (!CLIENT.user.loggedIn) {
				if (CLIENT.box1Active) {
					CLIENT.user.usernameBox += " ";
				} else if (CLIENT.box2Active) {
					CLIENT.user.password += " ";
					CLIENT.user.passwordBox += "*";
				}
			} else if (CLIENT.user.typing) {
				if (CLIENT.user.message.length() < 120)
					CLIENT.user.message += " ";
			}
			break;
		case KeyEvent.VK_A:
			if (!CLIENT.user.typing) {
				CLIENT.user.playerX--;
			}
			break;
		case KeyEvent.VK_D:
			if (!CLIENT.user.typing) {
				CLIENT.user.playerX++;
			}
			break;
		case KeyEvent.VK_W:
			if (!CLIENT.user.typing) {
				CLIENT.user.playerY++;
			}
			break;
		case KeyEvent.VK_S:
			if (!CLIENT.user.typing) {
				CLIENT.user.playerY--;
			}
			break;
		default:
			if (!CLIENT.user.loggedIn) {
				if (CLIENT.box1Active) {
					CLIENT.user.usernameBox += arg0.getKeyChar();
				} else if (CLIENT.box2Active) {
					CLIENT.user.password += arg0.getKeyChar();
					CLIENT.user.passwordBox += "*";
				}
			} else if (CLIENT.user.typing) {
				if (CLIENT.user.message.length() < 120)
					CLIENT.user.message += arg0.getKeyChar();
				CLIENT.user.message = CLIENT.user.message.substring(0, 1)
						.toUpperCase() + CLIENT.user.message.substring(1);
			}
		}
	}
}
