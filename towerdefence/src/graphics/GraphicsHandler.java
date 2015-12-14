package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import main.Game;
import util.CArray;
import entities.AnimalCell;
import entities.PlantCell;

public class GraphicsHandler extends Component {

	private static final long serialVersionUID = 1L;

	public static Image makeColorTransparent(final BufferedImage im,
			final Color color) {
		final ImageFilter filter = new RGBImageFilter() {
			public int markerRGB = color.getRGB() | 0xFFFFFFFF;

			public final int filterRGB(final int x, final int y, final int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		final ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	public AnimalCell[] aC = new AnimalCell[0];
	public int[] animalCellChanges = { 0, 0, 0, 1, 1, 1, 2, 2, 3, 4, 5, 6, 6,
			5, 4, 3, 2, 2, 1, 1, 1, 0, 0, 0 };
	private int animalCellsLoaded = 0;
	public BasicSprite[] background = new BasicSprite[0];
	public BasicSprite[] button = new BasicSprite[5];
	public Dimension d = null;
	public int doActionTo = 0;
	public AnimalCell doActionToAC = null;
	public BasicSprite doActionToTile = null;
	public Game game = null;
	protected Image[] images = new Image[0];
	private Image tileImage;
	private BasicSprite tileSprite;
	public int[] noChange = { 0, 0 };
	public boolean options = false;
	public boolean optionsToTile = false;
	public PlantCell[] pC = new PlantCell[25];
	private int plantCellsLoaded = 0;
	protected String[] resources = new String[0];
	public int selectedIndex = 0, selectedX = 0, selectedY = 0;
	public BasicSprite selection = null;
	private int spritesLoaded = 0;
	public CArray<GTile> tiles = new CArray<GTile>(0);
	private int tilesLoaded = 0;
	private int tillSort = 10;

	public GraphicsHandler(Game g) {
		game = g;
		for (int i = 0; i < pC.length; i++) {
			addPlantCell("Cache/Sprites/Orbs/PlantCell2.png", 0, 32);
		}
		tileImage = getImageForResource(game.TILE_SPRITE);
		tileSprite = new BasicSprite(tileImage, 0, 0);
		tiles.deserializeArray("MapTiles");
	}

	public void addAnimalCell(String img, int x, int y) {
		addAnimalCell(img, x, y, noChange, animalCellChanges);
	}

	public void addAnimalCell(String img, int x, int y, int[] stagesX,
			int[] stagesY) {
		this.aC = Arrays.copyOfRange(aC, 0, aC.length + 1);
		for (int i = 0; i < aC.length; i++) {
			if (aC[i] == null) {
				aC[i] = new AnimalCell(getImageForResource(img), x, y);
				aC[i].s.setUse(true);
				aC[i].s.xStages = stagesX;
				aC[i].s.yStages = stagesY;
				aC[i].s.stagingSpriteX = true;
				aC[i].s.stagingSpriteY = true;
				aC[i].addGH(this);
				getOnScreens();
				return;
			}
		}
		System.err.println("NONE WERE NULL!");
	}

	public void addAnimalCell(String[] imgs, int x, int y) {
		this.aC = Arrays.copyOfRange(aC, 0, aC.length + 1);
		for (int i = 0; i < aC.length; i++) {
			if (aC[i] == null) {
				aC[i] = new AnimalCell(getImagesForResources(imgs), x, y, 1.5);
				aC[i].s.setUse(true);
				aC[i].addGH(this);
				getOnScreens();
				return;
			}
		}
		System.err.println("NONE WERE NULL!");
	}

	public void addBack(String img, int x, int y) {
		this.background = Arrays.copyOfRange(background, 0,
				background.length + 1);
		background[background.length - 1] = new BasicSprite(
				getImageForResource(img), x, y);
		background[background.length - 1].setUse(true);
	}

	public void addBack(String[] imgs, int x, int y) {
		this.background = Arrays.copyOfRange(background, 0,
				background.length + 1);
		background[background.length - 1] = new BasicSprite(
				getImagesForResources(imgs), x, y, 1.5);
		background[background.length - 1].setUse(true);
	}

	public void addPlantCell(String img, int x, int y) {
		for (int i = 0; i < pC.length; i++) {
			if (pC[i] == null) {
				pC[i] = new PlantCell(getImageForResource(img), x, y);
				pC[i].s.setUse(false);
				pC[i].addGH(this);
				return;
			}
		}
		System.err.println("NONE WERE NULL!");
	}

	public void addPlantCell(String img, int x, int y, int[] stagesX,
			int[] stagesY) {
		for (int i = 0; i < pC.length; i++) {
			if (pC[i] == null) {
				pC[i] = new PlantCell(getImageForResource(img), x, y);
				pC[i].s.setUse(false);
				pC[i].s.xStages = stagesX;
				pC[i].s.stagingSpriteX = true;
				pC[i].addGH(this);
				return;
			}
		}
		System.err.println("NONE WERE NULL!");
	}

	public void addPlantCell(String[] imgs, int x, int y) {
		for (int i = 0; i < pC.length; i++) {
			if (pC[i] == null) {
				pC[i] = new PlantCell(getImagesForResources(imgs), x, y, 1.5);
				pC[i].s.setUse(false);
				pC[i].addGH(this);
				return;
			}
		}
		System.err.println("NONE WERE NULL!");
	}

	public void addSelection(String[] imgs, int x, int y) {
		selection = new BasicSprite(getImagesForResources(imgs), x, y, 1);
		selection.setUse(false);
	}

	public void addTile(int x, int y) {
		GTile newTile = new GTile(x, y);
		newTile.setUse(true);
		tiles.add(newTile);
		getOnScreens();
	}

	public void drawOptionAnimalCell(int i) {
		optionsToTile = false;
		options = true;
		doActionTo = i;
	}

	public void drawOptionsTile(int i) {
		optionsToTile = true;
		options = true;
		doActionTo = i;
	}

	public boolean drawPointer() {
		for (int i = 0; i < pC.length; i++) {
			if (pC[i].isOnScreen()) {
				return false;
			}
		}
		return true;
	}

	public boolean full(Object[] o) {
		for (Object obj : o) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}

	public BufferedImage getIcon(String resource) {
		try {
			return ImageIO.read(new File(resource));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public synchronized Image getImageForResource(String resource) {
		for (int i = 0; i < resources.length; i++) {
			try {
				if (resources[i].equals(resource)) {
					return images[i];
				}
			} catch (NullPointerException npe) {
				resources = Arrays.copyOfRange(resources, 0,
						resources.length + 1);
				images = Arrays.copyOfRange(images, 0, images.length + 1);
				for (int j = 0; j < resources.length; j++) {
					if (resources[j] == null && images[j] == null) {
						resources[j] = resource;
						images[j] = loadImage(resource);
						return images[j];
					}
				}
			}
		}
		resources = Arrays.copyOfRange(resources, 0, resources.length + 1);
		images = Arrays.copyOfRange(images, 0, images.length + 1);
		for (int i = 0; i < resources.length; i++) {
			if (resources[i] == null && images[i] == null) {
				resources[i] = resource;
				images[i] = loadImage(resource);
				return images[i];
			}
		}
		return null;
	}

	public synchronized Image[] getImagesForResources(String[] resource) {
		Image[] toReturn = new Image[resource.length];
		for (int i = 0; i < resources.length; i++) {
			for (int j = 0; j < resource.length; j++) {
				try {
					if (resources[i].equals(resource[j])) {
						toReturn[j] = images[i];
					}
				} catch (NullPointerException npe) {
					if (!full(toReturn)) {
						resources = Arrays.copyOfRange(resources, 0,
								resources.length + resource.length + 1);
						images = Arrays.copyOfRange(images, 0, images.length
								+ resource.length + 1);
						for (int k = 0; k < resource.length; k++) {
							for (int l = 0; l < resources.length; l++) {
								if (resources[l] == null && images[l] == null) {
									resources[l] = resource[k];
									images[l] = loadImage(resource[k]);
									toReturn[k] = images[l];
									l = resources.length;
								}
							}
						}
					}
				}
			}
		}
		if (!full(toReturn)) {
			resources = Arrays.copyOfRange(resources, 0, resources.length
					+ resource.length + 1);
			images = Arrays.copyOfRange(images, 0, images.length
					+ resource.length + 1);
			for (int i = 0; i < resource.length; i++) {
				for (int j = 0; j < resources.length; j++) {
					if (resources[j] == null && images[j] == null) {
						resources[j] = resource[i];
						images[j] = loadImage(resource[i]);
						toReturn[i] = images[j];
						j = resources.length;
					}
				}
			}
		}
		return toReturn;
	}

	public int getIndexOfPlantCellInArea(int x, int y, int x2, int y2) {
		for (int i = 0; i < pC.length; i++) {
			if (pC[i] == null)
				continue;
			if (((pC[i].x <= x2 && pC[i].x >= x) || (pC[i].x + pC[i].x2 <= x2 && pC[i].x
					+ pC[i].x2 >= x))
					&& ((pC[i].y <= y2 && pC[i].y >= y) || (pC[i].y + pC[i].y2 <= y2 && pC[i].y
							+ pC[i].y2 >= y))) {
				return i;
			}
		}
		return -1;
	}

	public void getOnScreens() {
		for (int i = 0; i < aC.length; i++) {
			if (aC[i] != null) {
				if (aC[i].isOnScreen()) {
					aC[i].onScreen = true;
				} else {
					aC[i].onScreen = false;
				}
			}
		}
		for (int i = 0; i < tiles.length(); i++) {
			if (tiles.get(i) != null) {
				if (isOnScreen(tileSprite, tiles.get(i).x - game.frameX,
						tiles.get(i).y - game.frameY)) {
					tiles.get(i).onScreen = true;
				} else {
					tiles.get(i).onScreen = false;
				}
			}
		}
	}

	public GTile getTile(int i) {
		return tiles.get(i);
	}

	public boolean isOnScreen(BasicSprite s, int x, int y) {
		return (x >= 0 && y >= 0 && x <= game.EDGE_OF_SCREEN.x && y <= game.EDGE_OF_SCREEN.y)
				|| (x + s.width() >= 0 && y + s.height() >= 0
						&& x + s.width() <= game.EDGE_OF_SCREEN.x && y
						+ s.height() <= game.EDGE_OF_SCREEN.y);
	}

	public synchronized Image loadImage(String resource) {
		if (getImageForResource(resource) == null) {
			System.out.println("MAKING IMAGE");
			try {
				return makeColorTransparent(ImageIO.read(new File(resource)),
						Color.WHITE);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void deleteTile() {
		tiles.removeIndex(game.gmh.getIndexOfTileInClickArea(game.MOUSE.x,
				game.MOUSE.y));
		tiles.cleanData();
	}

	public void paint(Graphics g) {
		spritesLoaded = 0;
		tilesLoaded = 0;
		animalCellsLoaded = 0;
		plantCellsLoaded = 0;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setClip(0, 0, 1028, 700);
		g2d.setColor(Color.black);
		for (BasicSprite back : background) {
			try {
				if (back.inUse()) {
					spritesLoaded++;
					g2d.drawImage(back.getNextImage(),
							back.x + back.getNextX(true),
							back.y + back.getNextY(true), null);
				}
			} catch (NullPointerException npe) {

			}
		}
		for (int i = 0; i < tiles.length(); i++) {
			if (tiles.get(i) == null)
				continue;
			if (tiles.get(i).inUse() && tiles.get(i).onScreen) {
				spritesLoaded++;
				tilesLoaded++;
				g2d.drawImage(tileImage, tiles.get(i).x - game.frameX,
						tiles.get(i).y - game.frameY, null);
			}
		}
		for (int i = 0; i < aC.length; i++) {
			if (aC[i] == null)
				continue;
			if (aC[i].inUse()) {
				if (aC[i].onScreen) {
					spritesLoaded++;
					animalCellsLoaded++;
					g2d.drawImage(aC[i].getNextImage(), aC[i].getX(true),
							aC[i].getY(true), null);
					if (aC[i].selected && selection.inUse() && !optionsToTile
							&& selectedIndex == i) {
						g2d.setColor(Color.MAGENTA);
						g2d.drawRect((aC[i].x - game.frameX)
								- ((aC[i].level) * 2), (aC[i].y - game.frameY)
								- ((aC[i].level) * 2), (aC[i].x2 - game.frameX)
								+ ((aC[i].level) * 4), (aC[i].y2 - game.frameY)
								+ ((aC[i].level)) * 4);
					}
				}
				if (aC[i].selected
						&& (!selection.inUse() || optionsToTile || selectedIndex != i)) {
					aC[i].selected = false;
				}
				try {
					if (aC[i].isAttacking()
							&& (pC[aC[i].plantIndex].isOnScreen() || aC[i].onScreen)) {
						g2d.setColor(Color.RED);
						g2d.drawLine(pC[aC[i].plantIndex].middle().x,
								pC[aC[i].plantIndex].middle().y,
								aC[i].middle().x, aC[i].middle().y);
						g2d.drawLine(pC[aC[i].plantIndex].middle().x - 1,
								pC[aC[i].plantIndex].middle().y,
								aC[i].middle().x - 1, aC[i].middle().y);
						g2d.drawLine(pC[aC[i].plantIndex].middle().x + 1,
								pC[aC[i].plantIndex].middle().y,
								aC[i].middle().x + 1, aC[i].middle().y);
						g2d.drawLine(pC[aC[i].plantIndex].middle().x,
								pC[aC[i].plantIndex].middle().y - 1,
								aC[i].middle().x, aC[i].middle().y - 1);
						g2d.drawLine(pC[aC[i].plantIndex].middle().x,
								pC[aC[i].plantIndex].middle().y + 1,
								aC[i].middle().x, aC[i].middle().y + 1);
					}
				} catch (Exception e) {

				}
				if (game.started)
					aC[i].attack();
				if (g2d.getColor() != Color.RED)
					g2d.setColor(Color.RED);
			}
		}
		for (int i = 0; i < pC.length; i++) {
			if (pC[i] == null) {

			} else if (pC[i].inUse() && !pC[i].isDead()) {
				if (game.started)
					pC[i].followPath();
				if (pC[i].isOnScreen()) {
					spritesLoaded++;
					plantCellsLoaded++;
					g2d.drawImage(pC[i].getNextImage(), pC[i].getX(true),
							pC[i].getY(true), null);
				}
			} else {
			}
		}
		try {
			if (selection.inUse()) {
				g2d.drawImage(selection.getNextImage(), selection.retrieveX()
						- 1 - game.frameX, selection.retrieveY() - 1
						- game.frameY, null);
			} else {
			}
		} catch (Exception e) {

		}
		if (game.DIAGNOSTIC_MODE) {
			g2d.setColor(Color.MAGENTA);
			for (int i = 0; i < game.map.map.length; i++) {
				for (int j = 0; j < game.map.map[i].length; j++) {
					if (game.map.map[i][j].x - game.frameX >= -32
							&& game.map.map[i][j].x - game.frameX <= 1024
							&& game.map.map[i][j].y - game.frameY >= -32
							&& game.map.map[i][j].y - game.frameY <= 700)
						g2d.drawRect(game.map.map[i][j].x - game.frameX,
								game.map.map[i][j].y - game.frameY, 32, 32);
				}
			}
		}
		try {
			g2d.drawImage(button[0].getNextImage(),
					button[0].x + button[0].getNextX(false), button[0].y
							+ button[0].getNextY(false), null);
			g2d.drawImage(button[2].getNextImage(),
					button[2].x + button[2].getNextX(false), button[2].y
							+ button[2].getNextY(false), null);
			g2d.drawImage(button[1].getNextImage(),
					button[1].x + button[1].getNextX(false), button[1].y
							+ button[1].getNextY(false), null);
			g2d.drawImage(button[3].getNextImage(),
					button[3].x + button[3].getNextX(false), button[3].y
							+ button[3].getNextY(false), null);
			spritesLoaded += 4;
			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font(";p;", Font.BOLD, 12));

			g2d.drawString("Score: " + game.score, 6, 610);
			g2d.drawString("Life: " + game.life, 6, 620);
			g2d.drawString("Money: $" + game.money, 6, 630);
			g2d.drawString("Mouse: (" + game.MOUSE.x + ", " + game.MOUSE.y
					+ ")",
					(int) (1024 - (("Mouse: (" + game.MOUSE.x + ", "
							+ game.MOUSE.y + ")").length() * (g2d.getFont()
							.getSize() * 0.75))), 10);
			g2d.drawString(
					"Tile: ("
							+ game.map.getTileContainingPoint(game.MOUSE).x
							+ ", "
							+ (game.map.getTileContainingPoint(game.MOUSE).y - 32)
							+ ")",
					(int) (1024 - (("Tile: ("
							+ game.map.getTileContainingPoint(game.MOUSE).x
							+ ", "
							+ (game.map.getTileContainingPoint(game.MOUSE).y - 32) + ")")
							.length() * (g2d.getFont().getSize() * 0.75))), 20);
			g2d.drawString("Loaded  : " + spritesLoaded,
					(int) (1024 - (("Loaded: " + spritesLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 30);
			g2d.drawString(
					"TLength : " + tiles.length(),
					(int) (1024 - (("TLength : " + tiles.length()).length() * (g2d
							.getFont().getSize() * 0.75))), 40);
			g2d.drawString("TLoaded : " + tilesLoaded,
					(int) (1024 - (("TLoaded : " + tilesLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 50);
			g2d.drawString("ACLength: " + aC.length,
					(int) (1024 - (("ACLength: " + aC.length).length() * (g2d
							.getFont().getSize() * 0.75))), 60);
			g2d.drawString(
					"ACLoaded: " + animalCellsLoaded,
					(int) (1024 - (("ACLoaded: " + animalCellsLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 70);
			g2d.drawString("PCLength: " + pC.length,
					(int) (1024 - (("PCLength: " + pC.length).length() * (g2d
							.getFont().getSize() * 0.75))), 80);
			g2d.drawString(
					"PCLoaded: " + plantCellsLoaded,
					(int) (1024 - (("PCLoaded: " + plantCellsLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 90);

			g2d.setColor(Color.GREEN);
			g2d.setFont(new Font("Dialog", Font.PLAIN, 12));

			g2d.drawString("Score: " + game.score, 4, 610);
			g2d.drawString("Life: " + game.life, 4, 620);
			g2d.drawString("Money: $" + game.money, 4, 630);
			g2d.drawString("Mouse: (" + game.MOUSE.x + ", " + game.MOUSE.y
					+ ")",
					(int) (1024 - (("Mouse: (" + game.MOUSE.x + ", "
							+ game.MOUSE.y + ")").length() * (g2d.getFont()
							.getSize() * 0.75))), 10);
			g2d.drawString(
					"Tile: ("
							+ game.map.getTileContainingPoint(game.MOUSE).x
							+ ", "
							+ (game.map.getTileContainingPoint(game.MOUSE).y - 32)
							+ ")",
					(int) (1024 - (("Tile: ("
							+ game.map.getTileContainingPoint(game.MOUSE).x
							+ ", "
							+ (game.map.getTileContainingPoint(game.MOUSE).y - 32) + ")")
							.length() * (g2d.getFont().getSize() * 0.75))), 20);
			g2d.drawString("Loaded  : " + spritesLoaded,
					(int) (1024 - (("Loaded: " + spritesLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 30);
			g2d.drawString(
					"TLength : " + tiles.length(),
					(int) (1024 - (("TLength : " + tiles.length()).length() * (g2d
							.getFont().getSize() * 0.75))), 40);
			g2d.drawString("TLoaded : " + tilesLoaded,
					(int) (1024 - (("TLoaded : " + tilesLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 50);
			g2d.drawString("ACLength: " + aC.length,
					(int) (1024 - (("ACLength: " + aC.length).length() * (g2d
							.getFont().getSize() * 0.75))), 60);
			g2d.drawString(
					"ACLoaded: " + animalCellsLoaded,
					(int) (1024 - (("ACLoaded: " + animalCellsLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 70);
			g2d.drawString("PCLength: " + pC.length,
					(int) (1024 - (("PCLength: " + pC.length).length() * (g2d
							.getFont().getSize() * 0.75))), 80);
			g2d.drawString(
					"PCLoaded: " + plantCellsLoaded,
					(int) (1024 - (("PCLoaded: " + plantCellsLoaded).length() * (g2d
							.getFont().getSize() * 0.75))), 90);

			g2d.setColor(Color.BLACK);
			g2d.setFont(new Font("Dialog", Font.BOLD, 12));

			g2d.drawString("Sell", button[0].x + button[0].getNextX(false) + 7,
					button[0].y + button[0].getNextY(false) + 17);
			g2d.drawString("Upgrade", button[2].x + button[2].getNextX(false)
					+ 7, button[2].y + button[2].getNextY(false) + 17);
			g2d.drawString("Buy New: $10",
					button[1].x + button[1].getNextX(false) + 7, button[1].y
							+ button[1].getNextY(false) + 17);
			if (game.started)
				g2d.drawString("Pause", button[3].x + button[3].getNextX(false)
						+ 7, button[3].y + button[3].getNextY(false) + 17);
			else {
				g2d.drawString("Start", button[3].x + button[3].getNextX(false)
						+ 7, button[3].y + button[3].getNextY(false) + 17);
				g2d.setFont(new Font("Dialog", Font.BOLD, 32));
				g2d.drawString("PAUSED", 462, 300);
				g2d.setColor(Color.GREEN);
				g2d.setFont(new Font("Dialog", Font.PLAIN, 32));
				g2d.drawString("PAUSED", 462, 300);
			}
		} catch (Exception e) {

		}
	}

	public void reset() {
		for (int i = 0; i < pC.length; i++) {
			addPlantCell("Cache/Sprites/Orbs/PlantCell2.png", 0, 32);
		}
		aC = new AnimalCell[704];
	}

	public boolean roundOver() {
		for (PlantCell c : pC) {
			if (!c.isDead()) {
				return false;
			}
		}
		return true;
	}
}