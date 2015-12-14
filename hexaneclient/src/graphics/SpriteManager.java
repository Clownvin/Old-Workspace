package graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import util.InterfaceAlignment;
import client.ButtonEnum;
import client.Client;

public class SpriteManager {
	public final Client client;
	public ArrayList<Sprite> sprites = new ArrayList<Sprite>();
	public ArrayList<Integer> ids = new ArrayList<Integer>();
	public ArrayList<Sprite> interfaceSprites = new ArrayList<Sprite>();
	public ArrayList<Integer> ids2 = new ArrayList<Integer>();

	public SpriteManager(final Client client) {
		this.client = client;
	}

	public void addSprite(Sprite sprite, int id) {
		sprites.add(sprite);
		ids.add(id);
	}

	public void addSprite(int x, int y, int width, int height, int imageId,
			int id) {
		sprites.add(new Sprite(client, x, y, width, height, imageId));
		ids.add(id);
	}

	public void addSprite(int x, int y, int width, int height, int imageId,
			int id, InterfaceAlignment alignment) {
		sprites.add(new Sprite(client, x, y, width, height, imageId, alignment));
		ids.add(id);
	}

	public void addSprite(int x, int y, int imageId, int id) {
		sprites.add(new Sprite(client, x, y, imageId));
		ids.add(id);
	}

	public void addSprite(int x, int y, int imageId, int id,
			InterfaceAlignment alignment) {
		sprites.add(new Sprite(client, x, y, imageId, alignment));
		ids.add(id);
	}

	public void addInterfaceSprite(Sprite sprite, int id) {
		interfaceSprites.add(sprite);
		ids2.add(id);
	}

	public void addInterfaceSprite(int x, int y, int width, int height,
			int imageId, int id) {
		interfaceSprites.add(new Sprite(client, x, y, width, height, imageId));
		ids2.add(id);
	}

	public void addInterfaceSprite(int x, int y, int width, int height,
			int imageId, int id, InterfaceAlignment alignment) {
		interfaceSprites.add(new Sprite(client, x, y, width, height, imageId,
				alignment));
		ids2.add(id);
	}

	public void addInterfaceSprite(int x, int y, int imageId, int id) {
		interfaceSprites.add(new Sprite(client, x, y, imageId));
		ids2.add(id);
	}

	public void addInterfaceSprite(int x, int y, int imageId, int id,
			InterfaceAlignment alignment) {
		interfaceSprites.add(new Sprite(client, x, y, imageId, alignment));
		ids2.add(id);
	}

	@SuppressWarnings("static-access")
	public void loadInterfaceSprites() {
		addInterfaceSprite(2, -1, 2, 2, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(3, 1, 3, 1, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-450, -2, 0, 3, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-400, -2, 0, 4, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-350, -2, 0, 5, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-300, -2, 0, 6, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-250, -2, 0, 7, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-200, -2, 0, 8, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-150, -2, 0, 9, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-100, -2, 0, 10, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-50, -2, 0, 11, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(-2, -2, 0, 12, InterfaceAlignment.BOTTOM_RIGHT);
		addInterfaceSprite(0, 0, 4, 13, InterfaceAlignment.BOTTOM_LEFT);

		addInterfaceSprite(0, 0, 5, 14, InterfaceAlignment.CENTER);
		getInterfaceSprite(14).loginScreen = true;
		addInterfaceSprite(136, -106, 7, 15, InterfaceAlignment.CENTER);
		client.BUTTONS
				.addButton(getInterfaceSprite(15), ButtonEnum.REMEMBER_ME);
		getInterfaceSprite(15).loginScreen = true;
		getInterfaceSprite(15).mouseoverImage = ImageCache
				.getPermanentBufferedImage(13);
		addInterfaceSprite(-80, -106, 6, 16, InterfaceAlignment.CENTER);
		client.BUTTONS.addButton(getInterfaceSprite(16),
				ButtonEnum.USERNAME_BOX);
		getInterfaceSprite(16).loginScreen = true;
		getInterfaceSprite(16).mouseoverImage = ImageCache
				.getPermanentBufferedImage(9);
		addInterfaceSprite(-80, -50, 6, 17, InterfaceAlignment.CENTER);
		client.BUTTONS.addButton(getInterfaceSprite(17),
				ButtonEnum.PASSWORD_BOX);
		getInterfaceSprite(17).loginScreen = true;
		getInterfaceSprite(17).mouseoverImage = ImageCache
				.getPermanentBufferedImage(9);
		addInterfaceSprite(180, -50, 11, 18, InterfaceAlignment.CENTER);
		client.BUTTONS.addButton(getInterfaceSprite(18), ButtonEnum.LOG_IN);
		getInterfaceSprite(18).loginScreen = true;
		getInterfaceSprite(18).mouseoverImage = ImageCache
				.getPermanentBufferedImage(10);
		addInterfaceSprite(140, -110, 8, 19, InterfaceAlignment.CENTER);
		getInterfaceSprite(19).loginScreen = true;
		getInterfaceSprite(19).visible = client.user.rememberMe;
	}

	public void drawInterfaceSprites(Graphics g) {
		g.setColor(Color.CYAN);
		for (int i = 0; i < interfaceSprites.size(); i++) {
			interfaceSprites.get(i).paint(g);
		}
	}

	public Sprite getSprite(int id) {
		for (int i = 0; i < ids.size(); i++) {
			if (ids.get(i) == id) {
				return sprites.get(i);
			}
		}
		return null;
	}

	public Sprite getInterfaceSprite(int id) {
		for (int i = 0; i < ids2.size(); i++) {
			if (ids2.get(i) == id) {
				return interfaceSprites.get(i);
			}
		}
		return null;
	}
}
