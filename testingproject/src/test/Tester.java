package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Vector;

import com.limeapi.util.BinaryOperations;
import com.limeapi.util.CyclicArrayList;

public final class Tester {
    public static void main(String[] args) throws IOException {
	byte[][] $1gb = new byte[1][1073741816];
	for (int i = 0; i < $1gb[0].length; i++) {
	    $1gb[0][i] = (byte) (i % 0xFF);
	}
	writeFormattedBytes($1gb, "1GB File.dat");
    }
    
    public static void writeFormattedBytes(byte[][] byteBlocks, String fileName) throws IOException {
	OutputStream out = new FileOutputStream(new File(fileName));
	out.write(BinaryOperations.toBytes(byteBlocks.length));
	for (byte[] block : byteBlocks) {
	    out.write(BinaryOperations.toBytes(block.length));
	    out.write(block);
	}
	out.close();
    }
    
    public static byte[][] readFormattedBytes(String fileName) throws IOException {
	System.out.println("Attempting to read bytes...");
	InputStream in = new FileInputStream(new File(fileName));
	byte[] lenBuffer = new byte[4];
	if (in.read(lenBuffer) >= 4) {
	    byte[][] byteBlocks = new byte[BinaryOperations.bytesToInteger(lenBuffer)][0];
	    int index = 0;
	    while (in.read(lenBuffer) >= 4 && index < byteBlocks.length) {
		byteBlocks[index] = new byte[BinaryOperations.bytesToInteger(lenBuffer)];
		if (in.read(byteBlocks[index]) < byteBlocks[index].length) {
		    System.err.println("May not have read the bytes properly");
		}
		System.out.println("Block "+index+":");
		for (byte b : byteBlocks[index]) {
		    System.out.print(b+" ");
		}
		System.out.println();
		index++;
	    }
	    in.close();
	    return byteBlocks;
	} else {
	    in.close();
	    return new byte[0][0];
	}
    }
}
