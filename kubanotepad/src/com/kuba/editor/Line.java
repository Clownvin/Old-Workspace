package com.kuba.editor;

import java.awt.Graphics;

import com.kuba.gui.ColorPalette;
import com.kuba.notepad.KubaNotepad;

public final class Line {
    private int index = 0;
    private String text = "";
    private byte mouseState = 0; //0: none; 1: mouse over; 2: selected:
    
    public Line(int index) {
	this.index = index;
    }
    
    public void paint(Graphics g) {
	switch (mouseState) {
	case 1:
	    g.setColor(ColorPalette.darken(KubaNotepad.getGUI().getBackground(), .3D));
	    g.fillRect(0, (index - KubaNotepad.getEditor().getIndexOffset()) * g.getFontMetrics().getHeight() , KubaNotepad.getGUI().getWidth(), g.getFontMetrics().getHeight());
	    break;
	case 2:
	    g.setColor(ColorPalette.darken(KubaNotepad.getGUI().getBackground(), .15D));
	    g.fillRect(0, (index - KubaNotepad.getEditor().getIndexOffset()) * g.getFontMetrics().getHeight(), KubaNotepad.getGUI().getWidth(), g.getFontMetrics().getHeight());
	    break;
	}
    }
    
    public void setMouseState(byte mouseState) {
	this.mouseState = mouseState;
    }
    
    public byte getMouseState() {
	return mouseState;
    }
}
