package graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import modeler.Face;
import modeler.Point3D;

/**
 * A class comprised of methods and containers to simulate layers on which models reside before being
 * rendered.
 * 
 * The lower the layer, the sooner it get's drawn.
 */
public class RenderingLayers {
	private final ArrayList<ArrayList<Face>> layers;
	private final int layerCount;
	private int[] facesPerLayer;
	private int totalFaces = 0;
	
	public RenderingLayers(int layerCount) {
		this.layers = new ArrayList<ArrayList<Face>>(layerCount);
		this.layerCount = layerCount;
		for (int i = 0; i < layerCount; i++) {
			layers.add(new ArrayList<Face>()); // Initializing face list lists
		}
		facesPerLayer = new int[layerCount];
		System.out.println("Size: "+layers.size() );
	}
	
	public void queueFacesToLayer(Face[] faces, int layer) {
		if (layer < 0 || layer >= layerCount) {
			throw new IllegalArgumentException("Layer index must fit within bounds.");
		}
		for (Face face : faces) {
			layers.get(layer).add(face);
		}
		totalFaces += faces.length;
		facesPerLayer[layer] += faces.length;
	}
	
	public ArrayList<ArrayList<Face>> getLayers() {
		return layers;
	}
	
	private static int distance(Point3D p1, Point3D p2) {
		return (int) (Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
	}
	
	public Face[] grabQueuedFaces(Point3D p) {
		Face[] allFaces = new Face[totalFaces];
		int pointer = 0;
		for (ArrayList<Face> layer : layers) {
			Face[] layerFaces = new Face[layer.size()];
			for (Face face : sortByDistance(layer.toArray(layerFaces), p)) {
				allFaces[pointer++] = face;
			}
		}
		return allFaces;
	}
	
	private static Face[] sortByDistance(Face[] faces, final Point3D p) {
		final Comparator<Face> faceComparator = new Comparator<Face>() {
			@Override
			public int compare(Face o1, Face o2) {
				return   distance(o2.getAveragePoint(), p) - distance(o1.getAveragePoint(), p);
			}
		};
		Arrays.sort(faces, faceComparator);
		return faces;
	}
	
	public void clear() {
		for (int i = 0; i < layers.size(); i++) {
			layers.get(i).clear();
		}
	}
}
