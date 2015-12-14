package world;

import modeler.Face;

public class GameObject {
    private Face[] faces = new Face[128];
    int x, y;
    int pointer;

    public void addFace(Face face) {
	faces[pointer] = face;
	pointer++;
    }
}
