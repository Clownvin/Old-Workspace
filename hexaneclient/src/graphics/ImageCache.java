package graphics;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javaxt.io.Image;

public final class ImageCache {

	private static ArrayList<Image> IMAGES = new ArrayList<Image>();
	private static ArrayList<Image> PERMANENT_IMAGES = new ArrayList<Image>();
	private static final ImageCache SINGLETON;

	static {
		SINGLETON = new ImageCache();
	}

	private ImageCache() {

	}

	public static ImageCache getSingleton() {
		return SINGLETON;
	}

	public static void loadImage(String path) {
		IMAGES.add(new Image(path));
	}

	public static void clearCache() {
		IMAGES.clear();
	}

	public static void loadPermanentImage(String path) {
		PERMANENT_IMAGES.add(new Image(path));
	}

	public static BufferedImage getBufferedImage(int index) {
		if (index >= IMAGES.size())
			throw new ArrayIndexOutOfBoundsException("Index " + index
					+ " is out of bounds. Must be less than " + IMAGES.size()
					+ ".");
		return IMAGES.get(index).getBufferedImage();
	}

	public static BufferedImage getPermanentBufferedImage(int index) {
		if (index >= PERMANENT_IMAGES.size())
			throw new ArrayIndexOutOfBoundsException("Index " + index
					+ " is out of bounds. Must be less than "
					+ PERMANENT_IMAGES.size() + ".");
		return PERMANENT_IMAGES.get(index).getBufferedImage();
	}

	public static Image getImage(int index) {
		if (index >= IMAGES.size())
			throw new ArrayIndexOutOfBoundsException("Index " + index
					+ " is out of bounds. Must be less than " + IMAGES.size()
					+ ".");
		return IMAGES.get(index);
	}

	public static Image getPermanentImage(int index) {
		if (index >= PERMANENT_IMAGES.size())
			throw new ArrayIndexOutOfBoundsException("Index " + index
					+ " is out of bounds. Must be less than "
					+ PERMANENT_IMAGES.size() + ".");
		return PERMANENT_IMAGES.get(index);
	}

	public static void loadCache(String path) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					path));
			byte[][][] imageCache = new byte[2][][];
			imageCache = (byte[][][]) in.readObject();
			for (int i = 0; i < imageCache[0].length; i++) {
				IMAGES.add(new Image(imageCache[0][i]));
			}
			for (int i = 0; i < imageCache[1].length; i++) {
				PERMANENT_IMAGES.add(new Image(imageCache[1][i]));
			}
			in.close();
			System.out.println("Loaded Image Cache OK.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Exception. Cache file could not be found.", "Exception",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Exception. I/OException occured.", "Exception",
					JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Exception. Class not found.",
					"Exception", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	public static void writeCache() {
		byte[][] bytes = new byte[IMAGES.size()][];
		byte[][] permaBytes = new byte[PERMANENT_IMAGES.size()][];
		byte[][][] imageCache = new byte[2][][];

		for (int i = 0; i < IMAGES.size(); i++) {
			bytes[i] = IMAGES.get(i).getByteArray("png");
		}

		for (int i = 0; i < PERMANENT_IMAGES.size(); i++) {
			permaBytes[i] = PERMANENT_IMAGES.get(i).getByteArray("png");
		}

		imageCache[0] = bytes;
		imageCache[1] = permaBytes;

		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream("./Cache.dat"));
			out.writeObject(imageCache);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
