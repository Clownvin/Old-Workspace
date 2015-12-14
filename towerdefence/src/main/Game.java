package main;

import entities.Cell;
import graphics.BasicSprite;
import graphics.GraphicsHandler;
import graphics.InfoMenu;
import graphics.LoadingBack;
import graphics.MenuHandler;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import sound.MP3;
import threading.CThreader;
import world.BasicMap;
import world.MapLoader;
import world.PathLoader;
import world.Point;

public class Game {

	public static void main(String[] args) {
		Game g = new Game();
		try {
			g.loadMenu();
		} catch (Exception e) {
		}
	}

	public GraphicsHandler graphicsHandler = new GraphicsHandler(this);
	public final boolean DIAGNOSTIC_MODE = true;
	public final CThreader THREADER = new CThreader();
	public JFrame died = new JFrame("Oh no! You died!");
	public final Point EDGE_OF_SCREEN = new Point(1024, 700);
	public JFrame f = new JFrame("Animal Cells vs Plant Cells");
	public int firstPause = 1;
	public int frameX = 2496;
	public int frameY = 2496;
	public boolean gameOver = false;
	public GraphicsMouseHandler gmh = null;
	public final JFrame INFO_MENU = new JFrame(
			"Animal Cells vs. Plant Cells - Info");
	public Point lastClicked;
	public int life = 10;

	public final BasicMap<Cell> map = new BasicMap<Cell>(200, 200, 32, 32);

	public MapLoader mapLoad;

	public final int MAX_FRAME_X = 4992;

	public final int MAX_FRAME_Y = 4992;

	public MenuMouseHandler mh = new MenuMouseHandler(this);

	public int money = 120; // 60
	public final Point MOUSE = new Point(0, 0);
	private final MP3 MUSIC = new MP3("Cache/Music/Hardstyle February 2012.mp3");
	public boolean needsStarted = false;
	private int newWave = 500;
	public PathLoader path = new PathLoader();
	public int random;
	public Round round;
	public int roundLife = 4;
	public int roundMoney = 3;
	public int roundScore = 10;
	public int score = 0;
	public final JFrame START_MENU = new JFrame(
			"Animal Cells vs. Plant Cells - Menu");
	public boolean started = false;

	public final String TILE_SPRITE = "Cache/Sprites/Frame/Tile.png";
	public JFrame whileLoading = new JFrame("Loading...");

	@SuppressWarnings("static-access")
	public synchronized void loadDead(int score) throws InterruptedException {
		MenuMouseHandler mmh = new MenuMouseHandler(this);
		final Canvas preC = new Canvas();
		final MenuHandler handler = new MenuHandler(preC);
		mmh.setMenuHandler(handler);
		Dimension dim = new Dimension(600, 200);
		died.setResizable(false);
		died.setMaximumSize(dim);
		died.setMinimumSize(dim);
		died.setSize(dim);
		died.add(preC);
		died.add(handler);
		died.addMouseListener(mmh);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (600 / 2);
		int Y = (screen.height / 2) - (200 / 2);
		died.setBounds(X, Y, 600, 200);
		died.pack();
		died.setVisible(true);
		died.setAlwaysOnTop(true);
		handler.button[0] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				325, 180);
		handler.score = score;
		died.setDefaultCloseOperation(died.DISPOSE_ON_CLOSE);
		while (died.isVisible()) {
			died.repaint();
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("static-access")
	public void loadInfoMenu() {
		final Canvas preC = new Canvas();

		Dimension dim = new Dimension(600, 600);
		final InfoMenu info = new InfoMenu();
		INFO_MENU.setResizable(false);
		INFO_MENU.setMaximumSize(dim);
		INFO_MENU.setMinimumSize(dim);
		INFO_MENU.setSize(dim);
		INFO_MENU.add(preC);
		INFO_MENU.add(info);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (600 / 2);
		int Y = (screen.height / 2) - (600 / 2);
		INFO_MENU.setBounds(X, Y, 600, 600);
		INFO_MENU.pack();
		INFO_MENU.setVisible(true);
		INFO_MENU.setDefaultCloseOperation(INFO_MENU.DISPOSE_ON_CLOSE);
	}

	@SuppressWarnings({ "static-access" })
	public synchronized void loadMenu() throws InterruptedException {
		mh = new MenuMouseHandler(this);
		final Canvas preC = new Canvas();
		final MenuHandler handler = new MenuHandler(preC);
		mh.setMenuHandler(handler);
		Dimension dim = new Dimension(600, 200);
		START_MENU.setResizable(false);
		START_MENU.setMaximumSize(dim);
		START_MENU.setMinimumSize(dim);
		START_MENU.setSize(dim);
		START_MENU.add(preC);
		START_MENU.add(handler);
		START_MENU.addMouseListener(mh);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (600 / 2);
		int Y = (screen.height / 2) - (200 / 2);
		START_MENU.setBounds(X, Y, 600, 200);
		START_MENU.pack();
		START_MENU.setVisible(true);
		START_MENU.setDefaultCloseOperation(START_MENU.EXIT_ON_CLOSE);
		handler.button[0] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				104, 125);
		handler.button[1] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				10, 125);
		try {
			THREADER.run(new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						if (shouldStart())
							startGame();
						try {
							TimeUnit.MILLISECONDS.sleep(1);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}));
		} catch (IllegalThreadStateException itse) {

		}
	}

	public void reset() {
		/*
		 * Thread t = new Thread(new Runnable() {
		 * 
		 * @Override public void run() {
		 */
		life = 10;
		score = 0;
		money = 120; // 60
		firstPause = 1;
		roundScore = 10;
		roundLife = 8;
		roundMoney = 3;
		gameOver = false;
		started = false;
		graphicsHandler = null;
		path = new PathLoader();
		mapLoad = null;
		needsStarted = false;
		whileLoading = new JFrame("Loading...");
		f = new JFrame("Animal Cells Vs. Plant Cells");
		newWave = 5000;
		/*
		 * } }); t.setName("Reset"); THREADER.executeThread(t); t = null;
		 */
	}

	public synchronized boolean shouldStart() {
		return needsStarted;
	}

	/**
	 * @param args
	 */

	@SuppressWarnings("deprecation")
	public void start() throws InterruptedException {
		graphicsHandler = new GraphicsHandler(this);
		// startGameChecker.interrupt();
		/*
		 * PUBLIC_THREAD.secludeProcess(new Runnable(){
		 * 
		 * @Override public void run() { MUSIC.playLoop(); } });
		 */
		if (!DIAGNOSTIC_MODE)
			MUSIC.playLoop();
		// THREADER.executeStack();
		// MUSIC.play();
		START_MENU.setVisible(false);
		died = new JFrame("Oh no! You died!");

		final Canvas preC = new Canvas();
		Dimension d = new Dimension(1024, 700);

		Dimension dim = new Dimension(600, 200);
		final LoadingBack g = new LoadingBack(preC, whileLoading, this);
		g.addBack(
				graphicsHandler.getImagesForResources(new String[] {
						"Cache/Sprites/Frame/Back0.png",
						"Cache/Sprites/Frame/Back1.png",
						"Cache/Sprites/Frame/Back2.png",
						"Cache/Sprites/Frame/Back3.png" }), 0, 0);
		whileLoading.setResizable(false);
		whileLoading.setMaximumSize(dim);
		whileLoading.setMinimumSize(dim);
		whileLoading.setSize(dim);
		whileLoading.add(preC);
		whileLoading.add(g);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (600 / 2);
		int Y = (screen.height / 2) - (200 / 2);
		whileLoading.setBounds(X, Y, 600, 200);
		whileLoading.pack();
		whileLoading.setVisible(true);
		THREADER.run(new Thread(new Runnable() {
			@Override
			public void run() {
				while (whileLoading.isVisible()) {
					whileLoading.repaint();
					try {
						TimeUnit.MILLISECONDS.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}));

		Canvas c = new Canvas();
		round = new Round(10, 24, 10, -32, path.paths[2].path[0][3],
				path.paths[2], this);
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Closed");
				System.exit(0);
			}
		});
		mapLoad = new MapLoader(graphicsHandler);
		mapLoad.loadMap();
		mapLoad = null;
		graphicsHandler.d = d;
		gmh = new GraphicsMouseHandler(map, graphicsHandler);
		// graphicsHandler.addBack("Cache/Sprites/Frame/Dash.png", 0, 580);
		graphicsHandler
				.addBack("Cache/Sprites/Frame/di-881D.jpg", -1248, -1248);
		graphicsHandler.button[0] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				0, 636);
		graphicsHandler.button[2] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				96, 636);
		graphicsHandler.button[1] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				192, 636);
		graphicsHandler.button[3] = new BasicSprite(
				graphicsHandler
						.getImageForResource("Cache/Sprites/Frame/button1.png"),
				288, 636);
		String[] selection = { "Cache/Sprites/Selection/selection1.png",
				"Cache/Sprites/Selection/selection2.png",
				"Cache/Sprites/Selection/selection3.png",
				"Cache/Sprites/Selection/selection4.png" };
		graphicsHandler.addSelection(selection, -2, -2);
		graphicsHandler.addTile(MAX_FRAME_X / 2 + (1024 / 2), MAX_FRAME_Y / 2
				+ (700 / 2));
		X = (screen.width / 2) - (1024 / 2);
		Y = (screen.height / 2) - (700 / 2);
		KeyHandler keyHandler = new KeyHandler(this, graphicsHandler);
		f.addKeyListener(keyHandler);
		f.setBounds(X, Y, 1024, 700);
		BufferedImage icon = graphicsHandler
				.getIcon("Cache/Sprites/Orbs/PlantCell.png");
		f.setIconImage(icon);
		f.setResizable(false);
		f.setMaximumSize(d);
		f.setMinimumSize(d);
		f.setSize(d);
		f.add(graphicsHandler);
		f.addMouseListener(gmh);
		f.addMouseMotionListener(gmh);
		f.pack();
		whileLoading.setVisible(false);
		// loading.interrupt();
		f.setVisible(true);
		while (!gameOver) {
			if (f.isActive()) {
				f.repaint();
				keyHandler.update();
				if (life <= 0)
					gameOver = true;
				if (started) {
					round.process();
					if (round.roundOver && newWave == 0) {
						gmh = new GraphicsMouseHandler(map, graphicsHandler);
						roundLife *= 1.3;
						roundScore *= 1.5;
						roundMoney *= 1.4;
						newWave = 10;
						System.out.println("Loading next  round");
						random = path.nextInt(0, 3);
						round = new Round(10, 24, roundLife, -32,
								path.paths[random].path[0][3],
								path.paths[random], this);
					} else if (round.roundOver && newWave != 0) {
						newWave--;
					}
				}
			}
			// }
			try {
				TimeUnit.MILLISECONDS.sleep(25);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		MUSIC.close();
		loadDead(score);
		f.dispose();
		f = null;
		reset();
		START_MENU.setVisible(true);
	}

	public void startGame() {

		System.out.println("Trying to start");
		try {
			if (shouldStart()) {
				needsStarted = false;
				start();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
