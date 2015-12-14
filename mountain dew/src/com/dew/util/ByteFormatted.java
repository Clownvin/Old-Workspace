package com.dew.util;

import com.dew.util.ByteFormatted;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface ByteFormatted<T extends ByteFormatted<?>> {
	public T fromBytes(byte[][] bytes);

	public byte[][] toBytes();
}
