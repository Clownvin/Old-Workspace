package graphics;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public enum GameFont {
	LOGIN_FONT, LOGIN_SCREEN_FONT, CHAT_BAR_FONT, CHAT_BOX_FONT;
	private static final Font[] FONTS = new Font[10];
	static {
		try {
			FONTS[0] = Font.createFont(Font.TRUETYPE_FONT, new File(
					"Minecraftia.ttf"));
			FONTS[1] = Font.createFont(Font.TRUETYPE_FONT, new File(
					"SF Automaton Extended.ttf"));
			FONTS[2] = Font.createFont(Font.TRUETYPE_FONT, new File(
					"04B_03__.ttf"));
			FONTS[3] = Font.createFont(Font.TRUETYPE_FONT, new File(
					"runescape_uf.ttf"));
			FONTS[4] = Font.createFont(Font.TRUETYPE_FONT, new File(
					"neuropolitical rg.ttf"));
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error loading fonts");
			e.printStackTrace();
		}
	}

	public Font getFont() {
		switch (this) {
		case LOGIN_FONT:
			return FONTS[1].deriveFont(30f).deriveFont(Font.BOLD);
		case LOGIN_SCREEN_FONT:
			return FONTS[1].deriveFont(18f);
		case CHAT_BAR_FONT:
			return FONTS[1].deriveFont(16f);
		case CHAT_BOX_FONT:
			return FONTS[1].deriveFont(16f);
		default:
			return FONTS[1];
		}
	}
}
