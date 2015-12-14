package main;

import graphics.MenuHandler;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MenuMouseHandler extends Component implements MouseListener {

	private static final long serialVersionUID = 1L;
	public int button = -1;
	private MenuHandler g;
	public Game game;

	public MenuMouseHandler(Game game2) {
		game = game2;
	}

	public int getIndexOfButtonInClickArea(int x, int y) {
		x -= 5;
		if (x < 0)
			x = 0;
		y -= 30;
		if (y < 0)
			y = 0;
		for (int i = 0; i < g.button.length; i++) {
			if (g.button[i] == null)
				continue;
			if ((x <= g.button[i].x + g.button[i].width() && x >= g.button[i].x)
					&& (y <= g.button[i].y + g.button[i].height() && y >= g.button[i].y)) {
				button = i;
				return i;
			}
		}
		return -1;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		if (getIndexOfButtonInClickArea(x, y) >= 0) {
			int clicked = button;
			if (clicked == 0) {
				game.died.setVisible(false);
				game.loadInfoMenu();
			} else if (clicked == 1) {
				game.mh.setVisible(false);
				game.needsStarted = true;
			}
			g.options = false;
			g.optionsToTile = false;
			if (game.DIAGNOSTIC_MODE)
				System.out.println("Button used is " + clicked);
		}

	}

	public void setMenuHandler(MenuHandler _g) {
		g = _g;
	}
}
