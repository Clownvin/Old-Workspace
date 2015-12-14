package client;

import graphics.Sprite;

import java.util.ArrayList;

public class ButtonManager {
	public final Client client;
	public ArrayList<InterfaceButton> buttons = new ArrayList<InterfaceButton>();

	public ButtonManager(final Client _client) {
		client = _client;
	}

	public void addButton(Sprite sprite, ButtonEnum buttonEnum) {
		buttons.add(new InterfaceButton(client, sprite, buttonEnum));
	}

	public InterfaceButton getButton(ButtonEnum buttonEnum) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).button == buttonEnum) {
				return buttons.get(i);
			}
		}
		throw new NullPointerException("Could not find matching button.");
	}

	public void handleMouseover(int x, int y) {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).toggleMouseover(x, y);
		}
	}

	public void handleClick(int x, int y) {
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).toggleClick(x, y);
		}
	}
}
