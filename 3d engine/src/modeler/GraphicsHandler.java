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
import modeler.Point3D;
import client.Client;

//TODO After you figure out the cam pos, and fix the clipping, revise ALL of this. It's messy af.
//Also, the camera moves when you change screen size. PROBABLY just not accomodating differing sizes, or something. Idk.
public final class GraphicsHandler extends Component {
    public final Client client;
    private int numberOfFacesDrawn = 0;
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
    
    private int getEuclideanDistance(float x1, float y1, float z1, float cameraX2, float cameraY2, float camZ2) {
	return (int) (Math.pow(cameraX2 - x1, 2) + Math.pow(cameraY2 - y1, 2) + Math.pow(camZ2 - z1,  2));
    }
    
    private Comparator<Face> faceSortByEuclideanDistance = new Comparator<Face>() {
	@Override
	public int compare(Face arg0, Face arg1) {
	    if (arg0 == null || arg1 == null)
		return 0;
	    return getEuclideanDistance(arg1.averageX, arg1.averageY, arg1.averageZ, cameraX, cameraY, camZ) + getEuclideanDistance(arg0.averageX, arg0.averageY, arg0.averageZ, cameraX, cameraY, camZ);
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
	queueFaceList(new Model3D().getFaces());
	queueFaceList(client.WORLD.getGroundFaces());
    }

    public void queueFaceList(Face[] faceList) {
	FACE_LISTS.add(faceList);
    }
    
    //NOTES:
    /*
     * results[0] = (int) (xScreenCenter - modelScale * temp * x); ------ Will invert view
     * Removing X/Y values causes trippy effects.
     * ROTATES around the xScreenCenter thing. Use that as ref point for camera?
     * ^^^^ May need to inverse the algorithm to get the 3D coords, if that's even possible.
     */
    public void renderFace() {
	xScreenCenter = client.width / 2;
	yScreenCenter = client.height / 2;
	double x = screenPosition.x + x0 * CT - y0 * ST;
	double y = screenPosition.y + x0 * STSP + y0 * CTSP + z0 * CP;
	double temp = viewAngle.z
		/ (screenPosition.z + x0 * STCP + y0 * CTCP - z0 * SP);
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
	Arrays.sort(output, faceSortByEuclideanDistance);
	return output;
    }
    
    public float getRefAngle(float angle) {
    	angle %= 360f;
    	if ((angle - 1) < 90) {
    		return angle;
    	}
    	if ((angle - 1) < 180) {
    		return 180f - angle;
    	}
    	if ((angle -181) < 90) {
    		return angle - 180f;
    	}
    	return 360f - angle;
    }
    
    public int getQuadrant(float angle) {
    	angle %= 360f;
    	if ((angle - 1) < 90) {
    		return 1;
    	}
    	if ((angle - 1) < 180) {
    		return 2;
    	}
    	if ((angle -181) < 90) {
    		return 3;
    	}
    	return 4;
    }
    float cameraY= 0, cameraX = 0, camZ = 0;

    public void renderFaceLists(Graphics g) {
	int sum = 0;
	int counter = 0;
	int hX, hY, lX, lY;
	cameraY= 0;
	cameraX = 0;
	theta = (DEG_TO_RAD * viewAngle.x) % (360 * DEG_TO_RAD);
	phi = (DEG_TO_RAD * viewAngle.y) % (360 * DEG_TO_RAD);
	//System.out.println("Theta: "+theta % 360+", viewAng.x: "+viewAngle.x % 360);
	//System.out.println("Phi: "+phi % 360+", viewAng.y: "+viewAngle.y % 360);
	camZ = (int) ((screenPosition.z / 2) * Math.sin(phi)); // NOTE: Good to go! :D
	int camRad = (int) ((int) ((screenPosition.z / 2) * Math.cos(phi)) * 1.15); // NOTE: This is the radius of the cam "Track". It is off, but only by about 200ish, or 15% (Guess)
	float refAngle = getRefAngle((float) viewAngle.x);
	switch (getQuadrant((float) viewAngle.x)) {
	case 1:
		cameraY = (float) (camRad * Math.sin(refAngle * DEG_TO_RAD)) * -1;
		cameraX = (float) (camRad * Math.cos(refAngle * DEG_TO_RAD)) * -1;
		break;
	case 2:
		cameraY = (float) (camRad * Math.sin(refAngle * DEG_TO_RAD)) * -1;
		cameraX = (float) (camRad * Math.cos(refAngle * DEG_TO_RAD));
		break;
	case 3:
		cameraY = (float) (camRad * Math.sin(refAngle * DEG_TO_RAD));
		cameraX = (float) (camRad * Math.cos(refAngle * DEG_TO_RAD));
		break;
	case 4:
		cameraY = (float) (camRad * Math.sin(refAngle * DEG_TO_RAD));
		cameraX = (float) (camRad * Math.cos(refAngle * DEG_TO_RAD)) * -1;
		break;
	}
	System.out.println("CAM: ("+cameraX+", "+cameraY+", "+camZ+") RefAngle: "+refAngle+", Quadrant: "+getQuadrant((float) viewAngle.x)+", CamRad: "+camRad);
	// MORE TRIG! :D
	//System.out.println("Cam X: "+camX);
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
	offscreenGraphics.setColor(Color.DARK_GRAY);
	offscreenGraphics.fillRect(0, 0, client.width, client.height);
	Face[] reconstructedFaceList = reconstructFaceList();
	FACE_LISTS.clear();
	int widthFix = (int) (client.x + 480 - (.5 * client.width));
	int heightFix = (int) (client.y + 480 - (.5 * client.height));
	int pointCount = 0;
	for (Face face : reconstructedFaceList) {
	    pointCount = 0;
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
		x0 = p.getX() - widthFix;
		y0 = p.getY() - heightFix;
		z0 = p.getZ();
		renderFace();
		x[i] = results[0];
		y[i] = results[1];
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
		    && (((lX > 0 && hX < client.width) || (lY > 0 && hY < client.height)) || face.background);
	    if (draw) {
		counter++;
		offscreenGraphics.setColor(face.c);
		offscreenGraphics.fillPolygon(x, y, pointCount);
		offscreenGraphics.setColor(Color.BLACK);
		offscreenGraphics.drawPolygon(x, y, pointCount);
		x0 = face.averageX - widthFix;
		y0 = face.averageY - heightFix;
		z0 = face.averageZ;
		renderFace();
		offscreenGraphics.drawString(""+getEuclideanDistance(x0, y0, z0, cameraX, cameraY, camZ), results[0] - (offscreenGraphics.getFontMetrics().stringWidth(""+getEuclideanDistance(x0, y0, z0, cameraX, cameraY, camZ)) / 2), results[1]);
		//offscreenGraphics.drawString("( "+x0+", "+ y0+", "+ z0+")", results[0] - (offscreenGraphics.getFontMetrics().stringWidth("( "+x0+", "+ y0+", "+ z0+")") / 2), results[1]);
	    }
	}
	x0 = client.x - widthFix;
	y0 = client.y - heightFix;
	z0 = 0;
	renderFace();
	//START CURSOR
	offscreenGraphics.setColor(ColorPalette.PURPLE.getColor());
	offscreenGraphics.drawLine((client.width / 2) - 10, client.height / 2, (client.width / 2) + 10, client.height / 2);
	offscreenGraphics.drawLine((client.width / 2), (client.height / 2) - 10, (client.width / 2), (client.height / 2) + 10);
	offscreenGraphics.setColor(Color.WHITE);
	offscreenGraphics.drawLine((client.width / 2) - 10, (client.height / 2) - 1, (client.width / 2) + 10, (client.height / 2) + 1);
	offscreenGraphics.drawLine((client.width / 2) - 1, (client.height / 2) - 10, (client.width / 2) + 1, (client.height / 2) + 10);
	offscreenGraphics.drawLine((client.width / 2) - 10, (client.height / 2)+1, (client.width / 2) + 10, (client.height / 2) -1);
	offscreenGraphics.drawLine((client.width / 2) + 1, (client.height / 2) - 10, (client.width / 2) - 1, (client.height / 2) + 10);
	offscreenGraphics.setColor(Color.BLACK);
	offscreenGraphics.drawLine((client.width / 2), (client.height / 2), (client.width / 2), (client.height / 2));
	//END CURSOR
	offscreenGraphics.setColor(ColorPalette.SEAFOAM.getColor());
	offscreenGraphics.fillRect(results[0], results[1], 5, 5);
	offscreenGraphics.drawString("ClientPos", results[0], results[1]);
	//System.out.println("MX:"+client.x+";MY:"+client.y);
	g.drawImage(offscreenImage, 0, 0, null);
	// System.out.println("Number of faces drawn: " + counter);
	numberOfFacesDrawn = counter;
    }

    int fps = 0;
    int[] fpss = new int[2500];
    int index = 0;

    @Override
    public void paint(Graphics g) {
	FACE_LISTS.clear();
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
	g2d.setColor(Color.BLACK);
	g2d.setFont(font.deriveFont(Font.BOLD));
	g2d.drawString("				FPS: " + (fps), 9, 40);
	g2d.drawString("				NOF: " + numberOfFacesDrawn, 9, 50);
	g2d.setColor(Color.GRAY);
	g2d.setFont(font);
	g2d.drawString("				FPS: " + (fps), 9, 40);
	g2d.drawString("				NOF: " + numberOfFacesDrawn, 9, 50);
    }
}
