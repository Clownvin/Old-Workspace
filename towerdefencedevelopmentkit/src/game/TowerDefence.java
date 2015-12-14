package game;

import game2d.Entity;
import game2d.Event;
import game2d.GameTemplate;
import game2d.GridPainter;
import game2d.TiledGrid;
import graphics.CFrame;

import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

public class TowerDefence extends GameTemplate {
	protected final CFrame GAMEFRAME = new CFrame("Tower Defence");
	protected final TiledGrid GRID = new TiledGrid(1032, 700, 32, 32);
	protected final GridPainter GP = new GridPainter(GRID);
	protected final MouseInput MI = new MouseInput(this);

	@Override
	public void start() {
		ENTITIES.addLayer();
		this.ENTITIES.addLayer();
		this.RESOURCES.loadResources("resources.res");
		this.RESOURCES.loadSprites(this);
		this.PAINTER.addGridPainter(GP);
		GAMEFRAME.add(this.PAINTER);
		GAMEFRAME.addMouseListener(MI);
		GAMEFRAME.addMouseMotionListener(MI);
		Dimension d = new Dimension(1024, 700);
		GAMEFRAME.setMaximumSize(d);
		GAMEFRAME.setMinimumSize(d);
		GAMEFRAME.setResizable(false);
		GAMEFRAME.setSize(d);
		GAMEFRAME.setDefaultCloseOperation(GAMEFRAME.EXIT_ON_CLOSE);
		GAMEFRAME.pack();
		GAMEFRAME.setVisible(true);

		Particle newParticle = new Particle(0, "Particle", null);
		newParticle.setX(0);
		newParticle.setY(0);
		newParticle.setSpeed(4);
		ENTITIES.addEntity(1, newParticle);
		while (true) {
			GAMEFRAME.repaint();
			ENTITIES.event(new Event(0, null));
			try {
				TimeUnit.MILLISECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void generateResources() {
		this.RESOURCES.addResource(RESOURCES.new Resource("TILE1", 0,
				"Cache/Sprites/Frame/Tile.png", 0));
		this.RESOURCES.addResource(RESOURCES.new Resource("TILE2", 1,
				"Cache/Sprites/Frame/TileIdea1_0.png", 0));
		this.RESOURCES.loadSprites(this);
		this.RESOURCES.saveResourcesToFile("resources.res");
	}

	public void addTile(int x, int y) {
		if (this.ENTITIES.getAtLocation(0, x, y) != null) {
			this.ENTITIES.getLayer(0).removeEntity(
					this.ENTITIES.getAtLocation(0, x, y));
			this.ENTITIES.getLayer(0).cleanArray();
			return;
		}
		ENTITIES.getLayer(0)
				.removeEntity(ENTITIES.getEntityByName(0, "Entity"));
		ENTITIES.cleanLayers();
		Entity newEntity = new Entity(12, "Entity");
		newEntity.setHeight(32);
		newEntity.setWidth(32);
		newEntity.setX(x);
		newEntity.setY(y);
		ENTITIES.addEntity(0, newEntity);
		((Particle) ENTITIES.getEntityByName(1, "Particle")).follow(
				ENTITIES.getEntityByName(0, "Entity"), 4);
	}

	public static void main(String[] args) {
		TowerDefence game = new TowerDefence();
		game.start();
	}

}
