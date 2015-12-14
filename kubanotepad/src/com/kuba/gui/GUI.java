package com.kuba.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JFrame;

import com.kuba.editor.Editor;

public class GUI extends JFrame {
    private Color backgroundColor = Color.DARK_GRAY;
    
    public GUI() {
	this.setTitle("Kuba Editor : No file opened");
	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	int x = (screen.width / 2) - (screen.width / 4);
	int y = (screen.height / 2) - (screen.height / 4);
	this.setBounds(x, y, screen.width / 2, screen.height / 2);
	this.setDefaultCloseOperation(GUI.EXIT_ON_CLOSE);
	this.setMinimumSize(new Dimension((int)(screen.width * 0.30), (int)(screen.height * 0.30)));
	this.setSize((int)(screen.width * 0.6),(int) (screen.height * 0.6));
    }

    @Override
    public void paint(Graphics g) {
	g.setColor(backgroundColor);
	g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }
    
    public Color getBackgroundColor() {
	return backgroundColor;
    }
}
