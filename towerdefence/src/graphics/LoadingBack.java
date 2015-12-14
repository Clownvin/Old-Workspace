package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.JFrame;

import main.Game;

public class LoadingBack extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BasicSprite background;
	// public int backgroundLength = 0;
	private Game game;

	public LoadingBack(Canvas c, JFrame f, Game g) {
		game = g;
	}

	public void addBack(Image[] imgs, int x, int y) {
		background = new BasicSprite(imgs, x, y, 1.5);
		background.setUse(true);
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setClip(0, 0, 800, 700);
		g2d.setColor(Color.black);
		g2d.drawImage(background.getNextImage(),
				background.x + background.getNextX(false), background.y
						+ background.getNextY(false), null);
	}

}
