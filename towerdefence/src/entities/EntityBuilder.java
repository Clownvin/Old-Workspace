package entities;

import java.awt.Image;

public class EntityBuilder {

	private EntityBuilder() {

	}

	public Cell buildCell(Image image, int x, int y) {
		return new Cell(image, x, y);
	}
}
