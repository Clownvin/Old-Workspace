package com.ctp.util;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface ByteFormated <T extends ByteFormated<?>> {
	public T fromBytes(byte[][] bytes);

	public byte[][] toBytes();
}
