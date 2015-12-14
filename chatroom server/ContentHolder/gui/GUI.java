package com.dew.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.dew.io.InteractableManager;
import com.dew.io.MouseHandler;

public class GUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 122239787138341099L;
	private final InteractableManager interactableManager = new InteractableManager();
	private final MouseHandler mouseHandler = new MouseHandler(
			getInteractableManager());

	public void format() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width / 2) - (screen.width / 4);
		int y = (screen.height / 2) - (screen.height / 4);
		this.setBounds(x, y, screen.width / 2, screen.height / 2);
		this.setMinimumSize(new Dimension(screen.width / 2, screen.height / 2));
		this.setMaximumSize(new Dimension(screen.width, screen.height));
		this.setDefaultCloseOperation(GUI.EXIT_ON_CLOSE);
	}

	public GUI() {
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addKeyListener(getInteractableManager());
		format();
	}

	public void packGUI() {

	}

	public InteractableManager getInteractableManager() {
		return interactableManager;
	}
}
