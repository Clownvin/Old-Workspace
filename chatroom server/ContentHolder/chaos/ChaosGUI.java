package com.dew.chaos;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.dew.gui.ComponentLayout;
import com.dew.gui.GUI;
import com.dew.io.InteractableManager;
import com.dew.io.MouseHandler;

public final class ChaosGUI extends GUI implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5484651461727537578L;
	private final InteractableManager interactableManager = new InteractableManager();
	private final MouseHandler mouseHandler = new MouseHandler(
			interactableManager);
	private final Thread runner = new Thread(this);
	public final ComponentLayout loginLayout = new ComponentLayout(this);

	private ChaosGUI() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width / 2) - (screen.width / 4);
		int y = (screen.height / 2) - (screen.height / 4);
		this.setBounds(x, y, screen.width / 2, screen.height / 2);
		this.setMinimumSize(new Dimension(screen.width / 2, screen.height / 2));
		this.setMaximumSize(new Dimension(screen.width, screen.height));
		this.setDefaultCloseOperation(ChaosGUI.EXIT_ON_CLOSE);
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
		this.addKeyListener(interactableManager);
		this.add(loginLayout);
		this.setTitle("ChAoS Login");
		this.pack();
		runner.start();
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}
}
