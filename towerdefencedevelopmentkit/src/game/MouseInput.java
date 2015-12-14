package game;

import game2d.GameInput;
import geometry.CPoint;

import java.awt.event.MouseEvent;

public class MouseInput implements GameInput {
	protected int x, y;
	protected final TowerDefence DF;

	public MouseInput(final TowerDefence DF) {
		this.DF = DF;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
		int tileX = DF.GRID.getClickedTile(this.x, this.y).getX();
		int tileY = DF.GRID.getClickedTile(this.x, this.y).getY();
		DF.addTile(tileX, tileY);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		CPoint mouse = DF.GAMEFRAME.formatClick(new CPoint(e.getX(), e.getY()));
		this.x = mouse.getIntX();
		this.y = mouse.getIntY();
	}

	@Override
	public int getMouseX() {
		return this.x;
	}

	@Override
	public int getMouseY() {
		return this.y;
	}

}
