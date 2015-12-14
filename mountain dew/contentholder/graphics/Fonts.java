package com.dew.graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public enum Fonts {
	MAIN("./Font/LondonMM.ttf");
	private Font font;

	private Fonts(String path) {
		try {
			this.font = Font.createFont(Font.TRUETYPE_FONT, new File(path));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}

	public Font getFont() {
		return font;
	}
}
