package modeler;

import java.awt.Color;

import com.engine.io.FileManager;
import com.engine.io.FileManagerWriteable;
import com.engine.io.FileType;
import com.engine.util.BinaryOperations;

import util.ColorPalette;


public class Model3D implements com.engine.io.FileManagerWriteable{
    private Face[] modelFaces = new Face[12];
    private int xOffset, yOffset, zOffset;
    
    public Model3D() {
	Color steel = ColorPalette.mixColors(Color.WHITE, Color.LIGHT_GRAY);
	Color gold = ColorPalette.mixColors(Color.YELLOW, Color.ORANGE);
	modelFaces = new Face[20];
	//				x                              y                              z
	modelFaces[0] = new Face(new int[] {0, 400, 400, 0}, new int[] {0, 0, -10, -5}, new int[] {0, 0, 40, 40}, steel);
	modelFaces[1] = new Face(new int[] {0, 400, 400, 0}, new int[] {-5, -10, 0, 0}, new int[] {40, 40, 120, 120}, steel);
	modelFaces[2] = new Face(new int[] {0, 400, 400, 0}, new int[] {5, 10, 0, 0}, new int[] {40, 40, 0, 0}, steel);
	modelFaces[3] = new Face(new int[] {0, 400, 400, 0}, new int[] {0, 0, 10, 5}, new int[] {120, 120, 40, 40}, steel);
	modelFaces[4] = new Face(new int[] {400, 600, 400}, new int[] {0, 0, -10}, new int[] {0, 120, 40}, steel);
	modelFaces[5] = new Face(new int[] {400, 600, 400}, new int[] {-10, 0, 0}, new int[] {40, 120, 120}, steel);
	modelFaces[6] = new Face(new int[] {400, 600, 400}, new int[] {10, 0, 0}, new int[] {40, 120, 0}, steel);
	modelFaces[7] = new Face(new int[] {400, 600, 400}, new int[] {0, 0, 10}, new int[] {120, 120, 40}, steel);
	modelFaces[8] = new Face(new int[] {0, -10, -40, 0}, new int[] {5, 5, 13, 17}, new int[] {-40, -40, 40, 40}, gold);
	modelFaces[9] = new Face(new int[] {0, -40, -40, 0}, new int[] {17, 13, 5, 7}, new int[] {40, 40, 140, 140}, gold);
	modelFaces[10] = new Face(new int[] {0, -40, -10, 0}, new int[] {-17, -13, -5, -5}, new int[] {40, 40, -40, -40}, gold);
	modelFaces[11] = new Face(new int[] {0, -40, -40, 0}, new int[] {-7, -5, -13, -17}, new int[] {140, 140, 40, 40}, gold);
	modelFaces[12] = new Face(new int[] {0, 0, 0, 0}, new int[] {-5, 5, 17, -17}, new int[] {-40, -40, 40, 40}, gold);
	modelFaces[13] = new Face(new int[] {0, 0, 0, 0}, new int[] {-17, 17, 7, -7}, new int[] {40, 40, 140, 140}, gold);
	modelFaces[14] = new Face(new int[] {0, 0, -40, -40}, new int[] {-7, 7, 5, -5}, new int[] {140, 140, 140, 140}, gold);
	modelFaces[15] = new Face(new int[] {-40, -40, -40, -40}, new int[] {-5, 5, 13, -13}, new int[] {140, 140, 40, 40}, gold);
	modelFaces[16] = new Face(new int[] {-10, -10, -40, -40}, new int[] {5, -5, -13, 13}, new int[] {-40, -40, 40, 40}, gold);
	modelFaces[17] = new Face(new int[] {-40, -40, -200, -200}, new int[] {5, -5, -5, 5}, new int[] {40, 40, 40, 40});
	modelFaces[18] = new Face(new int[] {-40, -40, -200, -200}, new int[] {-5, 5, 5, -5}, new int[] {60, 60, 60, 60});
	modelFaces[19] = new Face(new int[] {-40, -40, -200, -200}, new int[] {7, 5 ,5, 7}, new int[] {48, 40, 40, 48});
	FileManager.writeFile(this);
    }
    
    public Face[] getFaces() {
	return modelFaces;
    }

    @Override
    public FileManagerWriteable fromBytes(byte[][] bytes) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public byte[][] toBytes() {
	byte[][] bytes = new byte[modelFaces.length + 1][];
	bytes[0] = BinaryOperations.toBytes(modelFaces.length);
	for (int i = 1; i < modelFaces.length + 1; i++) {
	    bytes[i] = modelFaces[i-1].toBytes();
	}
	return bytes;
    }

    @Override
    public String getFileName() {
	return "BasicSword";
    }

    @Override
    public FileType getFileType() {
	return FileType.MODEL;
    }
}
