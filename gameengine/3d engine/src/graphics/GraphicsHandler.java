package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import util.ColorPalette;
import modeler.Face;
import modeler.Model3D;
import modeler.Point2D;
import modeler.Point3D;
import client.Client;

//TODO After you figure out the cam pos, and fix the clipping, revise ALL of this. It's messy af.
//Also, the camera moves when you change screen size. PROBABLY just not accomodating differing sizes, or something. Idk.
public final class GraphicsHandler extends Component {
	public final Client client;
	private int numberOfFacesDrawn = 0;
	private double theta = 0;
	private double phi = 0;
	private float ctcp = 0;
	private float ctsp = 0;
	private float stcp = 0;
	private float stsp = 0;
	private boolean draw = true;
	private Point3D p = null;
	private int[] x = new int[0];
	private int[] y = new int[0];
	private int facelength = 0;
	private int x0 = 0;
	private int y0 = 0;
	private int z0 = 0;
	float z1 = 0;
	private int i = 0;

	private int getEuclideanDistance(int x1, int y1, int z1, int x2, int y2,
			int z2) {
		return (int) (Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) + Math.pow(z2
				- z1, 2));
	}

	private Comparator<Face> faceSortByEuclideanDistance = new Comparator<Face>() {
		@Override
		public int compare(Face arg0, Face arg1) {
			return 0;
		}
	};

	private int[] results = new int[2];

	private int xScreenCenter = 0;
	private int yScreenCenter = 0;
	// Screen pos Z = Hypotenuse?
	private Point3D screenPosition = new Point3D(0, 0, 40 * 48);
	public Point3D viewAngle = new Point3D(0, 180 - 25, 400);
	private static final double DEG_TO_RAD = 0.017453292;
	private double modelScale = 10;
	private double CT = Math.cos(DEG_TO_RAD * viewAngle.x);
	private double ST = Math.sin(DEG_TO_RAD * viewAngle.x);
	private double CP = Math.cos(DEG_TO_RAD * viewAngle.y);
	private double SP = Math.sin(DEG_TO_RAD * viewAngle.y);
	private final RenderingLayers renderingLayers = new RenderingLayers(2);

	class FPSCounter extends Thread {
		private long lastTime;
		private double fps;

		@Override
		public void run() {
			while (true) {
				lastTime = System.nanoTime();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {

				}
				fps = 1000000000.0 / (System.nanoTime() - lastTime);
				lastTime = System.nanoTime();
			}
		}

		public double fps() {
			return fps;
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5236095033146766356L;
	FPSCounter counter = new FPSCounter();

	public GraphicsHandler(final Client client) {
		this.client = client;
		counter.start();
	}

	public void queueGround(Graphics g) {
		queueFaceList(new Model3D().getFaces(), 1);
		queueFaceList(client.WORLD.getGroundFaces(), 0);
	}

	public void queueFaceList(Face[] faceList, int layer) {
		renderingLayers.queueFacesToLayer(faceList, layer);
	}

	public void renderFaceLists(Graphics g) {
		client.testCamera.setScreenCenter(new Point2D(Client.width / 2, Client.height / 2));
		int sum = 0;
		int counter = 0;
		int hX, hY, lX, lY;
		theta = client.testCamera.getRadianTheta();
		phi = client.testCamera.getRadianPhi();
		CT = (float) Math.cos(theta);
		ST = (float) Math.sin(theta);
		CP = (float) Math.cos(phi);
		SP = (float) Math.sin(phi);
		ctcp = (float) (CT * CP);
		ctsp = (float) (CT * SP);
		stcp = (float) (ST * CP);
		stsp = (float) (ST * SP);
		BufferedImage offscreenImage = (BufferedImage) createImage(
				client.width, client.height);
		Graphics offscreenGraphics = offscreenImage.getGraphics();
		offscreenGraphics.setColor(Color.DARK_GRAY);
		offscreenGraphics.fillRect(0, 0, client.width, client.height);
		Face[] reconstructedFaceList = renderingLayers.grabQueuedFaces(client.testCamera.getCoordinates());
		int pointCount = 0;
		for (Face face : reconstructedFaceList) {
			pointCount = 0;
			draw = true;
			if (face == null)
				continue;
			if (facelength != face.getPoints().length) {
				facelength = face.getPoints().length;
				x = new int[facelength];
				y = new int[facelength];
			}
			for (i = 0; i < facelength; i++) {
				p = face.getPoints()[i];
				Point2D p2 = client.testCamera.translatePoint3D(p.setX(p.getX() - Client.x).setY(p.getY() - Client.y));
				x[i] = (int) p2.getX();
				y[i] = (int) p2.getY();
				pointCount++;
			}
			lX = Integer.MAX_VALUE;
			lY = Integer.MAX_VALUE;
			hX = Integer.MIN_VALUE;
			hY = Integer.MIN_VALUE;
			sum = 0;
			for (i = 0; i < facelength; i++) {
				sum += ((x[(i + 1) % facelength] - x[i]) * (y[(i + 1)
						% facelength] + y[i]));
				if (x[i] > hX) {
					hX = x[i];
				}
				if (x[i] < lX) {
					lX = x[i];
				}
				if (y[i] > hY) {
					hY = y[i];
				}
				if (y[i] < lY) {
					lY = y[i];
				}
			}
			draw = sum < 0
					&& (((lX < Client.width && hX > 0) && (lY < Client.height && hY > 0)));
			if (draw) {
				counter++;
				offscreenGraphics.setColor(face.getColor());
				offscreenGraphics.fillPolygon(x, y, pointCount);
				offscreenGraphics.setColor(Color.BLACK);
				offscreenGraphics.drawPolygon(x, y, pointCount);
				Point2D cam = client.testCamera.translatePoint3D(client.testCamera.getCoordinates());
				Point2D face22 = client.testCamera.translatePoint3D(face.getAveragePoint());
				offscreenGraphics.drawString(counter+"", (int)(face22.getX() - (offscreenGraphics.getFontMetrics().stringWidth(counter+"")/ 2)), (int)face22.getY());
				//offscreenGraphics.drawLine((int)face22.getX(),(int) face22.getY(), (int)cam.getX(), (int)cam.getY());
			}
		}
		g.drawImage(offscreenImage, 0, 0, null);
		numberOfFacesDrawn = counter;
		renderingLayers.clear();
	}

	int fps = 0;
	int[] fpss = new int[2500];
	int index = 0;

	@Override
	public void paint(Graphics g) {
		queueGround(g);
		counter.interrupt();
		fps = (int) (counter.fps());
		fpss[index] = fps;
		index++;
		if (index == 1000) {
			int number = 0;
			for (int i = 0; i < 1000; i++) {
				number += fpss[i];
			}
			System.out.println("Average FPS over 1k: " + (number / 1000));
			index = 0;
		}
		renderFaceLists(g);
		Graphics2D g2d = (Graphics2D) g;
		Font font = g2d.getFont();
		drawDebugInfo(g,"NOF: "+numberOfFacesDrawn, ""+client.testCamera.getCoordinates().getX()+", "+client.testCamera.getCoordinates().getY()+", "+client.testCamera.getCoordinates().getZ());
	}
	
	public void drawDebugInfo(Graphics g, String... strings) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < strings.length; i++) {
			g.drawString(strings[i], 8, (i * 10) + 39);
		}
		g.setColor(Color.GRAY);
		for (int i = 0; i < strings.length; i++) {
			g.drawString(strings[i], 9, (i * 10) + 40);
		}
	}
}
