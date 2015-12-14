package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class InfoMenu extends Component {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String[] info = new String[101];

	public InfoMenu() {
	}

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setClip(0, 0, 600, 600);
		g2d.setColor(Color.black);
		g2d.drawRect(0, 0, 800, 700);
		g2d.fillRect(0, 0, 800, 700);
		g2d.setColor(Color.GREEN);
		g2d.setFont(new Font("Dialogue", Font.BOLD, 14));
		g2d.drawString("Information", 230, 10);
		g2d.setFont(new Font("Dialogue", Font.PLAIN, 12));
		g2d.drawString("The objective of this game is to survive", 10, 20);
		g2d.drawString("for as long as possible. The enemy will", 10, 30);
		g2d.drawString("attempt to attack your core: The Mother Cell", 10, 40);
		g2d.drawString("Hotkeys:", 10, 50);
		g2d.drawString("UP/DOWN/LEFT/RIGHT: Movement of the camera.", 10, 60);
		g2d.drawString("P: Pause or start the game.", 10, 70);
		g2d.drawString("B: Buy new cells where you have selected.", 10, 80);
		g2d.drawString("U: Upgrade selected cell.", 10, 90);
		g2d.drawString("S: Sell selected cell.", 10, 100);
		/*
		 * if(game.diagnosticMode){ g2d.setColor(Color.MAGENTA); for(int i = 0;
		 * i < game.map.map.length; i++){ for(int j = 0; j <
		 * game.map.map[i].length; j++){ g2d.drawRect(game.map.map[i][j].x,
		 * game.map.map[i][j].y, 32, 32); } } }
		 */
	}
}
