package com.kuba.notepad;
import com.kuba.editor.Editor;
import com.kuba.gui.GUI;
public class KubaNotepad {
    private static final GUI mainGUI = new GUI();
    private static final Editor mainEditor = new Editor();
    
    public static void main(String[] args) throws InterruptedException {
	mainGUI.setVisible(true);
	while (true) {
	    mainGUI.repaint();
	}
    }
    
    public static Editor getEditor() {
	return mainEditor;
    }
    
    public static GUI getGUI() {
	return mainGUI;
    }
}
