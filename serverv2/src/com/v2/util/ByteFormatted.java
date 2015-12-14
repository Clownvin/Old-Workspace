package com.v2.util;

import com.v2.util.ByteFormatted;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface ByteFormatted<T extends ByteFormatted<?>> {
    public T fromBytes(byte[][] bytes);

    public byte[][] toBytes();
}
