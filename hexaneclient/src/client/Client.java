package client;

import graphics.GraphicsHandler;
import graphics.ImageCache;
import graphics.SpriteManager;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.hexane.packets.DoubleDataPacket;
import com.hexane.packets.Packet;
import com.hexane.packets.Request;
import com.hexane.packets.SingleDataPacket;

import packets.PacketHandler;
import user.User;
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
	public PacketHandler packetHandler = new PacketHandler(this);
	public int width, height;
	public boolean debug = true;
	public FontMetrics chatboxMetrics = null;
	public int mouseX;
	public int mouseY;
	public int cameraRotation = 0;
	public final Thread packetGrabber = new Thread(new Runnable() {

		@Override
		public void run() {
			boolean streamAlive = true;
			while (streamAlive) {
				try {
					Packet newPacket = (Packet) ois.readObject();
					packetHandler.handlePacket(newPacket);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
					streamAlive = false;
				} catch (IOException e) {
					streamAlive = false;
				}
				try {
					sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	});
	public final User user = new User(this);
	public final World WORLD = new World(this);
	public final ImageCache IMAGE_CACHE = ImageCache.getSingleton();
	public final JFrame GAMEFRAME = new JFrame("Incrisys 2D");
	public final ButtonManager BUTTONS = new ButtonManager(this);
	public final SpriteManager SPRITES = new SpriteManager(this);
	public final GraphicsHandler GRAPHICS_HANDLER = new GraphicsHandler(this);
	public final MouseHandler MOUSE_HANDLER = new MouseHandler(this);
	public final KeyHandler KEY_HANDLER = new KeyHandler(this);

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

	public void sendPacket(Packet p) {
		try {
			oos.writeObject(p);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error sending packet.");
		}
	}

	public void login() {
		DoubleDataPacket packet = new DoubleDataPacket();
		packet.request = Request.ATTEMPT_LOGIN;
		packet.data1.o = user.usernameBox;
		packet.data2.o = user.password;
		sendPacket(packet);
	}

	public void logout() {
		SingleDataPacket packet = new SingleDataPacket();
		packet.request = Request.LOGOUT;
		sendPacket(packet);
	}

	@Override
	public void run() {
		try {
			socket = new Socket("localhost", 2224);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error connecting to server. Try again later.");
			sendMessageFrame("Error connecting to server. Try again later.",
					"Error Connecting", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
			return;
		}
		packetGrabber.start();
		File settings = new File("./rememberMe.txt");
		if (settings.exists() && !settings.isDirectory()) {
			BufferedReader reader;
			try {
				reader = new BufferedReader(new FileReader("./rememberMe.txt"));
				String line1 = reader.readLine();
				if (line1 != null) {
					user.usernameBox = line1;
					user.rememberMe = true;
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int X = (screen.width / 2) - (1024 / 2);
		int Y = (screen.height / 2) - (700 / 2);
		GAMEFRAME.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logout();
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
		// GRAPHICS_HANDLER.loadFrame();
		ImageCache.loadCache("./Cache.dat");
		SPRITES.loadInterfaceSprites();
		GAMEFRAME.setIconImage(ImageCache.getPermanentBufferedImage(12));
		GAMEFRAME.add(GRAPHICS_HANDLER);
		GAMEFRAME.addMouseListener(MOUSE_HANDLER);
		GAMEFRAME.addMouseMotionListener(MOUSE_HANDLER);
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
