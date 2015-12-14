package entities;

import java.awt.Image;

import world.Path;
import world.Point;
import graphics.BasicSprite;

public interface Entity{
	public BasicSprite sprite = null;
	public int mapTile = 0;
	public Path path = null;
	public boolean inUse = false;
	
	public Point middle();
	
	public void followPath();
	
	public Image getNextImage();
}
