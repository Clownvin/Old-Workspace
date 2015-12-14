package graphics;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import util.Point3D;
import client.Client;

public final class GraphicsHandler extends Component {
	public final Client client;
	private double theta = 0;
	private double phi = 0;
	private float CTCP = 0;
	private float CTSP = 0;
	private float STCP = 0;
	private float STSP = 0;
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
	private Comparator<Face> faceSortByLowestZ = new Comparator<Face>() {
		@Override
		public int compare(Face arg0, Face arg1) {
			if (arg0 == null || arg1 == null)
				return 0;
			return arg0.lowestZ - arg1.lowestZ;
		}
	};

	private Comparator<Face> faceSortByHighestZ = new Comparator<Face>() {
		@Override
		public int compare(Face arg0, Face arg1) {
			if (arg0 == null || arg1 == null)
				return 0;
			return arg0.highestZ - arg1.highestZ;
		}
	};

	private int[] results = new int[2];

	private int xScreenCenter = 0;
	private int yScreenCenter = 0;
	private Point3D screenPosition = new Point3D(0, 0, 40 * 48);
	private Point3D viewAngle = new Point3D(0, 145, 180);
	private static final double DEG_TO_RAD = 0.017453292;
	private double modelScale = 10;
	private double CT = Math.cos(DEG_TO_RAD * viewAngle.getX());
	private double ST = Math.sin(DEG_TO_RAD * viewAngle.getX());
	private double CP = Math.cos(DEG_TO_RAD * viewAngle.getY());
	private double SP = Math.sin(DEG_TO_RAD * viewAngle.getY());

	private final ArrayList<Face[]> FACE_LISTS = new ArrayList<Face[]>();

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
		queueFaceList(client.WORLD.getGroundFaces());
	}

	public void queueFaceList(Face[] faceList) {
		FACE_LISTS.add(faceList);
	}

	public void renderFace() {
		xScreenCenter = client.width / 2;
		yScreenCenter = client.height / 2;
		double x = screenPosition.getX() + x0 * CT - y0 * ST;
		double y = screenPosition.getY() + x0 * STSP + y0 * CTSP + z0 * CP;
		double temp = viewAngle.getZ()
				/ (screenPosition.getZ() + x0 * STCP + y0 * CTCP - z0 * SP);
		results[0] = (int) (xScreenCenter + modelScale * temp * x);
		results[1] = (int) (yScreenCenter + modelScale * temp * y);
	}

	public Face[] reconstructFaceList() {
		int index = 0;
		int listIndex = 0;
		int totalSize = 0;
		for (i = 0; i < FACE_LISTS.size(); i++) {
			totalSize += FACE_LISTS.get(i).length;
		}
		Face[] output = new Face[totalSize];
		for (i = 0; i < totalSize; i++) {
			if (listIndex == FACE_LISTS.get(index).length) {
				index++;
				listIndex = 0;
				i--;
				continue;
			}
			if (FACE_LISTS.get(index)[listIndex] != null) {
				output[i] = FACE_LISTS.get(index)[listIndex];
			}
			listIndex++;
		}
		Arrays.sort(output, faceSortByLowestZ);
		Arrays.sort(output, faceSortByHighestZ);
		return output;
	}

	public void renderFaceLists(Graphics g) {
		int sum = 0;
		int count = 0;
		int hX, hY, lX, lY;
		theta = DEG_TO_RAD * viewAngle.getX();
		phi = DEG_TO_RAD * viewAngle.getY();
		CT = (float) Math.cos(theta);
		ST = (float) Math.sin(theta);
		CP = (float) Math.cos(phi);
		SP = (float) Math.sin(phi);
		CTCP = (float) (CT * CP);
		CTSP = (float) (CT * SP);
		STCP = (float) (ST * CP);
		STSP = (float) (ST * SP);
		BufferedImage offscreenImage = (BufferedImage) createImage(
				client.width, client.height);
		Graphics offscreenGraphics = offscreenImage.getGraphics();
		Face[] reconstructedFaceList = reconstructFaceList();
		FACE_LISTS.clear();
		for (Face face : reconstructedFaceList) {
			draw = true;
			if (face == null)
				continue;
			if (facelength != face.points.length) {
				facelength = face.points.length;
				x = new int[facelength];
				y = new int[facelength];
			}
			for (i = 0; i < facelength; i++) {
				p = face.points[i];
				x0 = p.getX();
				y0 = p.getY();
				z0 = p.getZ();
				renderFace();
				x[i] = results[0];
				y[i] = results[1];
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
			draw = sum < 0 && hX > 8 && lX < client.width - 40 && hY > 90
					&& lY < client.height - 40;
			if (draw) {
				offscreenGraphics.setColor(face.c);
				offscreenGraphics.fillPolygon(x, y, 4);
				offscreenGraphics.setColor(Color.BLACK);
				offscreenGraphics.drawPolygon(x, y, 4);
				count++;
			}
		}
		g.drawImage(offscreenImage, 0, 0, null);
	}

	int fps = 0;
	int[] fpss = new int[2500];
	int index = 0;

	@Override
	public void paint(Graphics g) {
		FACE_LISTS.clear();
		queueGround(g);
		renderFaceLists(g);
	}
}
