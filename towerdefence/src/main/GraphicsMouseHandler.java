package main;

import entities.Cell;
import graphics.GraphicsHandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import world.BasicMap;
import world.Point;

public class GraphicsMouseHandler extends ClickHandler implements
		MouseMotionListener, MouseListener {
	public int button = -1;
	private GraphicsHandler g;
	public int x;
	public int y;

	public GraphicsMouseHandler(BasicMap<Cell> m, GraphicsHandler _g) {
		super(m);
		g = _g;
	}

	public int getIndexOfButtonInClickArea(int x, int y) {
		x -= 3;
		if (x < 0)
			x = 0;
		y -= 26;
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

	public int getIndexOfCellInArea(int x1, int y1, int x2, int y2) {
		for (int i = 0; i < g.aC.length; i++) {
			if (g.aC[i] == null)
				continue;
			if ((g.aC[i].x >= x2 && g.aC[i].x <= x1)
					&& (g.aC[i].y >= y2 && g.aC[i].y <= y1)) {
				return i;
			}
		}
		return -1;
	}

	public int getIndexOfCellInClickArea(int x, int y) {
		x -= 3;
		if (x < 0)
			x = 0;
		y -= 26;
		if (y < 0)
			y = 0;
		for (int i = 0; i < g.aC.length; i++) {
			if (g.aC[i] == null)
				continue;
			if ((x <= g.aC[i].x + g.aC[i].s.width() - g.game.frameX && x >= g.aC[i].x
					- g.game.frameX)
					&& (y <= g.aC[i].y + g.aC[i].s.height() - g.game.frameY && y >= g.aC[i].y
							- g.game.frameY)) {
				return i;
			}
		}
		return -1;
	}

	public int getIndexOfTileInArea(int x1, int y1, int x2, int y2) {
		for (int i = 0; i < g.tiles.length(); i++) {
			if (g.tiles.get(i) == null)
				continue;
			if ((g.tiles.get(i).x >= x2 && g.tiles.get(i).x <= x1)
					&& (g.tiles.get(i).y >= y2 && g.tiles.get(i).y <= y1)) {
				return i;
			}
		}
		return -1;
	}

	public int getIndexOfTileInClickArea(int x, int y) {
		x -= 3;
		if (x < 0)
			x = 0;
		y -= 26;
		if (y < 0)
			y = 0;
		for (int i = 0; i < g.tiles.length(); i++) {
			if (g.tiles.get(i) == null)
				continue;
			if ((x <= (g.tiles.get(i).x + g.tiles.get(i).width())
					- g.game.frameX && x >= g.tiles.get(i).x - g.game.frameX)
					&& (y <= (g.tiles.get(i).y + g.tiles.get(i).height())
							- g.game.frameY && y >= g.tiles.get(i).y
							- g.game.frameY)) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		g.game.MOUSE.x = arg0.getX();
		g.game.MOUSE.y = arg0.getY();
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		x = arg0.getX();
		y = arg0.getY();
		Point oriented = new Point(x + g.game.frameX, y + g.game.frameY);
		if (g.game.lastClicked == null && !g.game.DIAGNOSTIC_MODE)
			g.game.lastClicked = new Point(
					g.game.map.getTileContainingPoint(g.game.MOUSE).x
							- g.game.frameX,
					g.game.map.getTileContainingPoint(g.game.MOUSE).y
							- g.game.frameY);
		else if (!g.game.DIAGNOSTIC_MODE) {
			int newX = 0;
			int newY = 0;
			if (g.game.lastClicked.x > g.game.map
					.getTileContainingPoint(g.game.MOUSE).x - g.game.frameX) {
				newX = -1;
			}
			if (g.game.lastClicked.x < g.game.map
					.getTileContainingPoint(g.game.MOUSE).x - g.game.frameX) {
				newX = 1;
			}
			if (g.game.lastClicked.y > g.game.map
					.getTileContainingPoint(g.game.MOUSE).y
					- 32
					- g.game.frameY) {
				newY = -1;
			}
			if (g.game.lastClicked.y < g.game.map
					.getTileContainingPoint(g.game.MOUSE).y
					- 32
					- g.game.frameY) {
				newY = 1;
			}
			System.out
					.println(",{"
							+ newX
							+ ", "
							+ newY
							+ ", "
							+ (g.game.map.getTileContainingPoint(g.game.MOUSE).x - g.game.frameX)
							+ ", "
							+ (g.game.map.getTileContainingPoint(g.game.MOUSE).y - 32 - g.game.frameY)
							+ "}");
			g.game.lastClicked.x = g.game.map
					.getTileContainingPoint(g.game.MOUSE).x - g.game.frameX;
			g.game.lastClicked.y = (g.game.map
					.getTileContainingPoint(g.game.MOUSE).y - 32)
					- g.game.frameY;
		}
		// System.out.println(getMapTileOfClickArea(x,y));
		// System.out.println("Mouse Released at ("+x+", "+y+")");
		if (getIndexOfCellInClickArea(x, y) >= 0
				&& (g.game.started || g.game.firstPause == 1)) {
			int clicked = getIndexOfCellInClickArea(x, y);
			int xValue = g.aC[clicked].x - g.game.frameX;
			int yValue = g.aC[clicked].y - g.game.frameY;
			g.aC[clicked].selected = true;
			g.selectedIndex = clicked;
			g.selection.x = xValue - 1;
			g.selection.y = yValue - 1;
			g.selection.usingXOf = g.aC[clicked].s;
			g.selection.usingYOf = g.aC[clicked].s;
			g.selection.useCellX = true;
			g.selection.useCellY = true;
			g.selection.setUse(true);
			g.drawOptionAnimalCell(clicked);
		} else if (getIndexOfTileInClickArea(x, y) >= 0
				&& (g.game.started || g.game.firstPause == 1)) {
			int clicked = getIndexOfTileInClickArea(x, y);
			int xValue = g.tiles.get(clicked).x - g.game.frameX;
			int yValue = g.tiles.get(clicked).y - g.game.frameY;
			g.selection.x = xValue - 1;
			g.selection.y = yValue - 1;
			g.selection.usingXOf = g.tiles.get(clicked);
			g.selection.usingYOf = g.tiles.get(clicked);
			g.selection.useCellX = true;
			g.selection.useCellY = true;
			g.selection.setUse(true);
			g.drawOptionsTile(clicked);

		} else if (getIndexOfButtonInClickArea(x, y) >= 0
				&& (g.game.started || g.game.firstPause == 1 || g.options || button == 3)) {
			int clicked = button;
			if (clicked == 0 && !g.optionsToTile && g.doActionTo != -1) {
				System.err.println("Clicked on sell...");
				if (!g.optionsToTile) {
					g.game.money += g.aC[g.doActionTo].value;
					g.aC[g.doActionTo] = null;
					g.doActionTo = -1;
				}
			} else if (clicked == 1 && g.optionsToTile && g.doActionTo != -1) {
				int xPos = g.tiles.get(g.doActionTo).x;
				int yPos = g.tiles.get(g.doActionTo).y;
				if (g.game.money >= 10) {
					g.addAnimalCell("Cache/Sprites/Orbs/AnimalCell.png", xPos,
							yPos);
					g.game.money -= 10;
				} else if (g.game.money < 10) {
					System.out.println("Not enough money! You have $"
							+ g.game.money);
				}
				g.doActionTo = -1;
			} else if (clicked == 2 && !g.optionsToTile && g.doActionTo != -1) {
				System.err.println("Trying to upgrade");
				System.out.println("Options to Tile is " + g.optionsToTile);
				if (!g.optionsToTile) {
					System.err.println("Upgrading aC[" + g.doActionTo
							+ "]. doActionTo = " + g.doActionTo);
					g.aC[g.doActionTo].upgrade();
				}
				g.doActionTo = -1;
			} else if (clicked == 3) {
				if (!g.game.started) {
					g.game.started = true;
					g.game.firstPause = 0;
				} else
					g.game.started = false;
				g.doActionTo = -1;
			}
			g.options = false;
			g.optionsToTile = false;
			System.out.println("Button used is " + clicked);
			g.selection.setUse(false);
			g.selection.useCellX = false;
			g.selection.useCellY = false;
		} else if (g.game.DIAGNOSTIC_MODE) {
			/*
			 * System.out.println("g.addTile(" +
			 * (g.game.map.getTileContainingPoint(oriented).x) + ", " +
			 * (g.game.map.getTileContainingPoint(oriented).y - 32) + ");");
			 */
			g.addTile(g.game.map.getTileContainingPoint(oriented).x,
					(g.game.map.getTileContainingPoint(oriented).y - 32));
		}

	}
}
