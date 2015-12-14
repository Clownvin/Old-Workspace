package client;

import graphics.Camera;
import graphics.GraphicsHandler;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import modeler.Point3D;
import world.World;

public final class Client extends Thread {
	public static int width;
	public static int height;
	public static int mouseX;
	public static int mouseY;
	public static final World WORLD = new World();
	public final JFrame GAMEFRAME = new JFrame("Incrisys 2D");
	public final GraphicsHandler GRAPHICS_HANDLER = new GraphicsHandler(this);
	public final MouseHandler MOUSE_HANDLER = new MouseHandler(this);
	public final KeyHandler KEY_HANDLER = new KeyHandler(this);
	public static int x = 0;
	public static int y = 0;

	public final Camera testCamera = new Camera();

	public static void main(String[] args) {
		Client c = new Client();
		c.start();
	}

	public void sendMessageFrame(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title,
				JOptionPane.PLAIN_MESSAGE);
	}

	public void sendMessageFrame(String message, String title, int type) {
		JOptionPane.showMessageDialog(null, message, title, type);
	}

	@Override
	public void run() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (1024 / 2);
		int Y = (screen.height / 2) - (700 / 2);
		GAMEFRAME.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Closed");
				System.exit(0);
			}
		});
		GAMEFRAME.add(GRAPHICS_HANDLER);
		GAMEFRAME.addMouseListener(MOUSE_HANDLER);
		GAMEFRAME.addMouseMotionListener(MOUSE_HANDLER);
		GAMEFRAME.addMouseWheelListener(MOUSE_HANDLER);
		GAMEFRAME.addKeyListener(KEY_HANDLER);
		GAMEFRAME.setBounds(X, Y, 1024, 700);
		GAMEFRAME.setMinimumSize(new Dimension(1024, 700));
		GAMEFRAME.setMaximumSize(new Dimension(1600, 900));
		GAMEFRAME.pack();
		GAMEFRAME.setVisible(true);
		while (true) {
			GAMEFRAME.repaint();
			width = GAMEFRAME.getWidth() - 16;
			height = GAMEFRAME.getHeight() - 39;
			try {
				sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
