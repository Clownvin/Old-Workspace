package com.dew.util;

import java.util.ArrayList;

import com.dew.lang.CorruptDataException;
import com.dew.util.BinaryOperations;

public final class Utilities {

	//TODO Document
	/**
	 * 
	 * @param base
	 * @param exponent
	 * @param modulus
	 * @return
	 */
	public static long modularPow(long base, long exponent, long modulus) {
		long result = 1;
		base %= modulus;
		while (exponent > 0) {
			if (exponent % 2 == 1) {
				result = (result * base) % modulus;
			}
			exponent = exponent >> 1;
			base = (base * base) % modulus;
		}
		return result;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[] streamFormat(byte[][] bytes) {
		int totalLength = 0;
		for (byte[] b : bytes) {
			if (b != null)
				totalLength += b.length + 4;
		}
		byte[] lengthBytes = BinaryOperations.toBytes(totalLength + 1);
		int index = 5;
		byte[] endBytes = new byte[totalLength + index];
		endBytes[0] = lengthBytes[0];
		endBytes[1] = lengthBytes[1];
		endBytes[2] = lengthBytes[2];
		endBytes[3] = lengthBytes[3];
		long cumulativeData = 0;
		for (byte[] b1 : bytes) {
			if (b1 != null) {
				for (byte b3 : BinaryOperations.toBytes(b1.length)) {
					endBytes[index] = b3;
					index++;
				}
				for (byte b2 : b1) {
					endBytes[index] = b2;
					cumulativeData += (b2 & 0xFF);
					index++;
				}
			}
		}
		endBytes[4] = (byte) (cumulativeData % 255);
		return endBytes;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte[][] streamUnformat(byte[] bytes) {
		ArrayList<byte[]> byteBlocks = new ArrayList<byte[]>();
		long cumulativeData = 0;
		for (int i = 1; i < bytes.length; i += 0) {
			byte[] block = new byte[BinaryOperations.bytesToInteger(bytes, i)];
			int idx = i += 4;
			for (int j = idx, k = 0; j < block.length + idx; j++, k++, i++) {
				try {
					block[k] = bytes[j];
					cumulativeData += (bytes[j] & 0xFF);
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println("Blocksize = " + block.length);
					System.out.println("Start index: "
							+ idx
							+ ", start bytes: "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx]))
							+ " "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx + 1]))
							+ " "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx + 2])));
					for (byte b : bytes)
						System.out.print((Integer.toHexString(
								Byte.toUnsignedInt(b)).length() < 2 ? "0"
								+ Integer.toHexString(Byte.toUnsignedInt(b))
								: Integer.toHexString(Byte.toUnsignedInt(b)))
								+ " ");
					System.out.println();
					return null;
				}
			}
			byteBlocks.add(block);
		}
		if ((cumulativeData % 255) != (bytes[0] & 0xFF)) {
			for (byte b : bytes)
				System.out.print((Integer.toHexString(Byte.toUnsignedInt(b))
						.length() < 2 ? "0"
						+ Integer.toHexString(Byte.toUnsignedInt(b)) : Integer
						.toHexString(Byte.toUnsignedInt(b)))
						+ " ");
			System.out.println();
			throw new CorruptDataException("Checksum evaluated to "
					+ (cumulativeData % 255)
					+ ", against the packet's checkdigit of "
					+ (bytes[0] & 0xFF));
		}
		byte[][] endByteBlocks = new byte[byteBlocks.size()][];
		int i = 0;
		for (byte[] b : byteBlocks) {
			endByteBlocks[i] = b;
			i++;
		}
		return endByteBlocks;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param incLengthBytes
	 * @return
	 */
	public static byte[][] streamUnformat(byte[] bytes, boolean incLengthBytes) {
		ArrayList<byte[]> byteBlocks = new ArrayList<byte[]>();
		long cumulativeData = 0;
		for (int i = (incLengthBytes ? 4 : 0) + 1; i < bytes.length; i += 0) {
			byte[] block = new byte[BinaryOperations.bytesToInteger(bytes, i)];
			int idx = i += 4;
			for (int j = idx, k = 0; j < block.length + idx; j++, k++, i++) {
				try {
					block[k] = bytes[j];
					cumulativeData += (bytes[j] & 0xFF);
				} catch (ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println("Blocksize = " + block.length);
					System.out.println("Start index: "
							+ idx
							+ ", start bytes: "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx]))
							+ " "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx + 1]))
							+ " "
							+ Integer.toHexString(Byte
									.toUnsignedInt(bytes[idx + 2])));
					for (byte b : bytes)
						System.out.print((Integer.toHexString(
								Byte.toUnsignedInt(b)).length() < 2 ? "0"
								+ Integer.toHexString(Byte.toUnsignedInt(b))
								: Integer.toHexString(Byte.toUnsignedInt(b)))
								+ " ");
					System.out.println();
					return null;
				}
			}
			byteBlocks.add(block);
		}
		if ((cumulativeData % 255) != (bytes[4] & 0xFF))
			throw new CorruptDataException("Checksum evaluated to "
					+ (cumulativeData % 255)
					+ ", against the packet's checkdigit of "
					+ (bytes[4] & 0xFF));
		byte[][] endByteBlocks = new byte[byteBlocks.size()][];
		int i = 0;
		for (byte[] b : byteBlocks) {
			endByteBlocks[i] = b;
			i++;
		}
		return endByteBlocks;
	}
}
