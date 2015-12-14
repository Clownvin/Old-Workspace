package client;

import graphics.Sprite;

public final class InterfaceButton {

	public final Client client;
	public Sprite sprite;
	public boolean clickTogglesMouseover = false;
	public final ButtonEnum button;

	public InterfaceButton(final Client client, Sprite sprite, ButtonEnum button) {
		this.client = client;
		this.sprite = sprite;
		this.button = button;
		switch (button) {
		case USERNAME_BOX:
			clickTogglesMouseover = true;
			break;
		case PASSWORD_BOX:
			clickTogglesMouseover = true;
			break;
		default:
			break;
		}
	}

	public void toggleMouseover(int x, int y) {
		if (sprite.loginScreen != client.user.loggedIn)
			if (sprite.coordinateInBounds(x, y)) {
				if (!clickTogglesMouseover) {
					sprite.mouseover = true;
				}
			} else {
				if (!clickTogglesMouseover) {
					sprite.mouseover = false;
				}
			}
	}

	public void toggleClick(int x, int y) {
		if (sprite.loginScreen != client.user.loggedIn)
			if (sprite.coordinateInBounds(x, y)) {
				if (clickTogglesMouseover) {
					sprite.mouseover = true;
				}
				switch (button) {
				case USERNAME_BOX:
					if (client.user.usernameBox.equalsIgnoreCase("username")
							&& !client.user.rememberMe
							&& !client.alreadyRemovedUser) {
						client.user.usernameBox = "";
						client.alreadyRemovedUser = true;
					}
					client.box1Active = true;
					break;
				case PASSWORD_BOX:
					if (client.user.passwordBox.equalsIgnoreCase("password")
							&& !client.alreadyRemovedPass) {
						client.user.passwordBox = "";
						client.alreadyRemovedPass = true;
					}
					client.box2Active = true;
					break;
				case LOG_IN:
					client.login();
					break;
				case REMEMBER_ME:
					if (client.user.rememberMe) {
						client.user.rememberMe = false;
						client.SPRITES.getInterfaceSprite(19).visible = false;
					} else {
						client.user.rememberMe = true;
						client.SPRITES.getInterfaceSprite(19).visible = true;
					}
					break;
				}
			} else {
				if (clickTogglesMouseover) {
					sprite.mouseover = false;
				}
				switch (button) {
				case USERNAME_BOX:
					client.box1Active = false;
					break;
				case PASSWORD_BOX:
					client.box2Active = false;
					break;
				default:
					break;
				}
			}
	}
}
