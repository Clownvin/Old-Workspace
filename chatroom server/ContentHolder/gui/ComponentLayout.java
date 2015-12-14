package com.dew.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;

import com.dew.graphics.Fonts;
import com.dew.io.Parentable;

public final class ComponentLayout extends Component implements Parentable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1998409773674873811L;
	private boolean visible = true;

	public ComponentLayout(JFrame jframe) {

	}

	private final ArrayList<GUIComponent> components = new ArrayList<GUIComponent>();

	public void addComponent(GUIComponent component) {
		component.setParent(this);
		components.add(component);
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void paint(Graphics g) {
		if (!visible)
			return;
		g.setFont(Fonts.MAIN.getFont().deriveFont(24.0f).deriveFont(Font.BOLD));
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		for (GUIComponent component : components) {
			if (component.isVisible()) {
				switch (component.getPosition()) {
				case CENTER:
					component.setX((getWidth() / 2)
							- (component.getWidth() / 2));
					component.setY((getHeight() / 2)
							- (component.getHeight() / 2));
					component.draw(g);
					break;
				case TOP_LEFT:
					component.setX(0);
					component.setY(0);
					component.draw(g);
					break;
				case TOP_RIGHT:
					component.setX(getWidth() - component.getWidth());
					component.setY(0);
					component.draw(g);
					break;
				case BOTTOM_LEFT:
					component.setX(0);
					component.setY(getHeight() - component.getHeight());
					component.draw(g);
					break;
				case BOTTOM_RIGHT:
					component.setX(getWidth() - component.getWidth());
					component.setY(getHeight() - component.getHeight());
					component.draw(g);
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public boolean hasParent() {
		return false;
	}

	@Override
	public Parentable getGUIParent() {
		return null;
	}
}
