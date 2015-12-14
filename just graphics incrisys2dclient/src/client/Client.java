package client;

import graphics.GraphicsHandler;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import world.World;

public final class Client extends Thread {
	public String loginError = "";
	public boolean box1Active = false;
	public boolean box2Active = false;
	public Socket socket = new Socket();
	public ObjectOutputStream oos;
	public ObjectInputStream ois;
	public boolean alreadyRemovedPass = false;
	public boolean alreadyRemovedUser = false;
	public int width, height;
	public boolean debug = true;
	public FontMetrics chatboxMetrics = null;
	public int mouseX;
	public int mouseY;
	public int cameraRotation = 0;
	public final World WORLD = new World(this);
	public final JFrame GAMEFRAME = new JFrame("Incrisys 2D");
	public final GraphicsHandler GRAPHICS_HANDLER = new GraphicsHandler(this);
	public final MouseHandler MOUSE_HANDLER = new MouseHandler(this);
	public int playerX = 0;
	public int playerY = 0;

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
		try {
			socket = new Socket("localhost", 43594);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error connecting to server. Try again later.");
			sendMessageFrame("Error connecting to server. Try again later.",
					"Error Connecting", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (1024 / 2);
		int Y = (screen.height / 2) - (700 / 2);
		GAMEFRAME.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					ois.close();
					oos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				System.out.println("Closed");
				System.exit(0);
			}
		});
		GAMEFRAME.add(GRAPHICS_HANDLER);
		GAMEFRAME.addMouseListener(MOUSE_HANDLER);
		GAMEFRAME.addMouseMotionListener(MOUSE_HANDLER);
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
