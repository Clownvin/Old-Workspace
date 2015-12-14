package com.engine.util;

import com.engine.util.ByteFormatted;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface ByteFormatted<T extends ByteFormatted<?>> {
	public T fromBytes(byte[][] bytes);

	public byte[][] toBytes();
}
