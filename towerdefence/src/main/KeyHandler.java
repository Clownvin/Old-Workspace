package main;

import graphics.GraphicsHandler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private Game g;
	private GraphicsHandler gh;

	private boolean pressingUp = false, pressingDown = false,
			pressingRight = false, pressingLeft = false, inControl = false;

	public KeyHandler(final Game _g, final GraphicsHandler _gh) {
		g = _g;
		gh = _gh;
	}

	public void up() {
		if (g.frameY - 32 >= 0) {
			g.frameY -= 32;
			gh.background[0].y += 16;
		} else if (g.frameY - 2 >= 0) {
			g.frameY -= 2;
			gh.background[0].y += 1;
		} else if (g.frameY > 0) {
			g.frameY--;
		}
		gh.getOnScreens();
	}

	public void down() {
		if (g.frameY + 32 <= g.MAX_FRAME_Y) {
			g.frameY += 32;
			gh.background[0].y -= 16;
		} else if (g.frameY + 2 <= g.MAX_FRAME_Y) {
			g.frameY += 2;
			gh.background[0].y -= 1;
		} else if (g.frameY < g.MAX_FRAME_Y) {
			g.frameY++;
		}
		gh.getOnScreens();
	}

	public void left() {
		if (g.frameX - 32 >= 0) {
			g.frameX -= 32;
			gh.background[0].x += 16;
		} else if (g.frameX - 2 >= 0) {
			g.frameX -= 2;
			gh.background[0].x += 1;
		} else if (g.frameX > 0) {
			g.frameX--;
		}
		gh.getOnScreens();
	}

	public void right() {
		if (g.frameX + 32 <= g.MAX_FRAME_X) {
			g.frameX += 32;
			gh.background[0].x -= 16;
		} else if (g.frameX + 2 <= g.MAX_FRAME_X) {
			g.frameX += 2;
			gh.background[0].x -= 1;
		} else if (g.frameX < g.MAX_FRAME_X) {
			g.frameX++;
		}
		gh.getOnScreens();
	}

	public void update() {
		if (pressingUp && !pressingDown)
			up();
		if (pressingDown && !pressingUp)
			down();
		if (pressingLeft && !pressingRight)
			left();
		if (pressingRight && !pressingLeft)
			right();
	}

	@SuppressWarnings("static-access")
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == e.VK_UP) {
			pressingUp = true;
		}
		if (key == e.VK_DOWN) {
			pressingDown = true;
		}
		if (key == e.VK_LEFT) {
			pressingLeft = true;
		}
		if (key == e.VK_RIGHT) {
			pressingRight = true;
		}
		if (key == e.VK_DELETE) {
			gh.deleteTile();
		}
		if (key == e.VK_CONTROL) {
			inControl = true;
		}
		if (key == e.VK_U) {
			if (!gh.optionsToTile && gh.doActionTo != -1) {
				gh.aC[gh.doActionTo].upgrade();
			}
			gh.doActionTo = -1;
			gh.options = false;
			gh.optionsToTile = false;
			gh.selection.setUse(false);
			gh.selection.useCellX = false;
			gh.selection.useCellY = false;
		}
		if (key == e.VK_P) {
			if (!g.started) {
				g.started = true;
				g.firstPause = 0;
			} else
				g.started = false;
			gh.doActionTo = -1;
			gh.options = false;
			gh.optionsToTile = false;
			gh.selection.setUse(false);
			gh.selection.useCellX = false;
			gh.selection.useCellY = false;
		}
		if (key == e.VK_B) {
			if (gh.doActionTo != -1) {
				try {
					if (gh.aC[gh.doActionTo].x != gh.tiles.get(gh.doActionTo).x
							&& gh.aC[gh.doActionTo].y != gh.tiles
									.get(gh.doActionTo).y) {
						int xPos = gh.tiles.get(gh.doActionTo).x;
						int yPos = gh.tiles.get(gh.doActionTo).y;
						if (g.money >= 10) {
							gh.addAnimalCell(
									"Cache/Sprites/Orbs/AnimalCell.png", xPos,
									yPos);
							g.money -= 10;
						}
						gh.doActionTo = -1;
						gh.options = false;
						gh.optionsToTile = false;
						gh.selection.setUse(false);
						gh.selection.useCellX = false;
						gh.selection.useCellY = false;
					}
				} catch (Exception e1) {
					int xPos = gh.tiles.get(gh.doActionTo).x;
					int yPos = gh.tiles.get(gh.doActionTo).y;
					if (g.money >= 10) {
						gh.addAnimalCell("Cache/Sprites/Orbs/AnimalCell.png",
								xPos, yPos);
						g.money -= 10;
					}
					gh.doActionTo = -1;
					gh.options = false;
					gh.optionsToTile = false;
					gh.selection.setUse(false);
					gh.selection.useCellX = false;
					gh.selection.useCellY = false;
				}
			}
		}
		if (key == e.VK_S) {
			if (inControl) {
				if (gh.game.DIAGNOSTIC_MODE) {
					gh.tiles.serializeArray("MapTiles");
					System.out.println("Map successfully saved!");
				}
			} else if (gh.doActionTo != -1) {
				if (!gh.optionsToTile) {
					gh.game.money += gh.aC[gh.doActionTo].value;
					gh.aC[gh.doActionTo] = null;
				}
				gh.doActionTo = -1;
				gh.options = false;
				gh.optionsToTile = false;
				gh.selection.setUse(false);
				gh.selection.useCellX = false;
				gh.selection.useCellY = false;
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == e.VK_UP)
			pressingUp = false;
		if (key == e.VK_DOWN)
			pressingDown = false;
		if (key == e.VK_LEFT)
			pressingLeft = false;
		if (key == e.VK_RIGHT)
			pressingRight = false;
		if (key == e.VK_CONTROL)
			inControl = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (g.DIAGNOSTIC_MODE)
			System.out.println(e.toString());
	}

}
