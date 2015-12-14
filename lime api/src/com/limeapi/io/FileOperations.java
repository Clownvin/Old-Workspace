package com.limeapi.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.limeapi.util.BinaryOperations;

public final class FileOperations {

    public static void writeBytesToFile(final String fileName,
	    byte[][] byteBlocks) throws IOException {
	OutputStream out = new FileOutputStream(new File(fileName));
	out.write(BinaryOperations.toBytes(byteBlocks.length));
	for (byte[] block : byteBlocks) {
	    out.write(BinaryOperations.toBytes(block.length));
	    out.write(block);
	}
	out.close();
    }

    public static byte[][] readBytesFromFile(final String fileName)
	    throws IOException {
	InputStream in = new FileInputStream(new File(fileName));
	byte[] lenBuffer = new byte[4];
	try {
	    if (in.read(lenBuffer) >= 4) {
		byte[][] byteBlocks = new byte[BinaryOperations
			.bytesToInteger(lenBuffer)][0];
		int index = 0;
		while (in.read(lenBuffer) >= 4 && index < byteBlocks.length) {
		    byteBlocks[index] = new byte[BinaryOperations
			    .bytesToInteger(lenBuffer)];
		    if (in.read(byteBlocks[index]) < byteBlocks[index].length) {
			System.err
				.println("May not have read the bytes properly.");
		    }
		    index++;
		}
		return byteBlocks;
	    } else {
		return new byte[0][0];
	    }
	} finally {
	    in.close();
	    in = null;
	}
    }

    // Write a "ByteFormatAlgorithm" type object which can be instantiated once,
    // and used as template instead.
    public static <T extends FileManagerWriteable> T readFile(T template,
	    String fileName) {
	if (template == null)
	    throw new IllegalArgumentException(
		    "Template must be a valid, instantiated object of the desired type, and cannot be null.");
	File file = new File(template.getFileType().getNormalFileDirectory()
		+ fileName + template.getFileType().getFileExtension());
	if (file.exists() && !file.isDirectory()) {
	    try {
		byte[][] bytes = readBytesFromFile(file.getAbsolutePath());
		Object o = template.fromBytes(bytes);
		if (o != null) {
		    @SuppressWarnings("unchecked")
		    T result = (T) o; // Only reason I split the return
				      // statement up was so I could throw in
				      // the unchecked annotation.
		    return result;
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }

    public static <T extends FileManagerWriteable> T readFile(T template,
	    String fileName, String path) {
	if (template == null)
	    throw new IllegalArgumentException(
		    "Template must be a valid, instantiated object of the desired type, and cannot be null.");
	File file = new File(path + fileName
		+ template.getFileType().getFileExtension());
	if (file.exists() && !file.isDirectory()) {
	    try {
		byte[][] bytes = readBytesFromFile(file.getAbsolutePath());
		Object o = template.fromBytes(bytes);
		if (o != null) {
		    @SuppressWarnings("unchecked")
		    T result = (T) o; // Only reason I split the return
				      // statement up was so I could throw in
				      // the unchecked annotation.
		    return result;
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }
    
    public static void writeFile(final FileManagerWriteable writeable) throws IOException {
	writeBytesToFile(writeable.getFileType().getNormalFileDirectory() + writeable.getFileName() + writeable.getFileType().getFileExtension(), writeable.toBytes());
    }
    
    public static void writeFile(final FileManagerWriteable writeable, final String path) throws IOException {
	writeBytesToFile(path + writeable.getFileName() + writeable.getFileType().getFileExtension(), writeable.toBytes());
    }
}
