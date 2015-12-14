package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

public class MenuHandler extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BasicSprite background;
	public int backgroundLength = 0;
	public BasicSprite[] button = new BasicSprite[2];
	public Canvas c;
	public int doActionTo = 0;
	public boolean options = false;
	public boolean optionsToTile = false;
	public int score = 0;

	public MenuHandler(Canvas _c) {
		c = _c;
	}

	public void addBack(Image[] imgs, int x, int y) {
		background = new BasicSprite(imgs, x, y, 1.5);
		background.setUse(true);
	}

	public void paint(Graphics g) {
		// System.out.println("Painting");
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, 600, 200);
		g2d.fillRect(0, 0, 600, 200);
		// g2d.drawImage(background.getNextImage(),background.x+background.getNextX(),background.y+background.getNextY(),null);
		try {
			g2d.setFont(new Font("Dialogue", Font.BOLD, 32));
			g2d.setColor(Color.RED);
			g2d.drawString("Animal Cells vs. Plant Cells", 50, 50);
			g2d.drawImage(button[0].getNextImage(),
					button[0].x + button[0].getNextX(false), button[0].y
							+ button[0].getNextY(false), null);
			g2d.drawImage(button[1].getNextImage(),
					button[1].x + button[1].getNextX(false), button[1].y
							+ button[1].getNextY(false), null);
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Dialogue", Font.BOLD, 12));
			g2d.drawString("Info", button[0].x + button[0].getNextX(false) + 7,
					button[0].y + button[0].getNextY(false) + 17);
			g2d.drawString("New Game", button[1].x + button[1].getNextX(false)
					+ 7, button[1].y + button[1].getNextY(false) + 17);
		} catch (NullPointerException e) {
			try {
				g2d.drawImage(button[0].getNextImage(),
						button[0].x + button[0].getNextX(false), button[0].y
								+ button[0].getNextY(false), null);
				g2d.setFont(new Font("Dialogue", Font.BOLD, 32));
				g2d.setColor(Color.RED);
				g2d.drawString("Score: " + score, 200, 100);
				g2d.setColor(Color.BLACK);
				g2d.setFont(new Font("Dialogue", Font.BOLD, 12));
				g2d.drawString("Okay", button[0].x + button[0].getNextX(false)
						+ 7, button[0].y + button[0].getNextY(false) + 17);
			} catch (Exception e2) {

			}
		}
	}
}
