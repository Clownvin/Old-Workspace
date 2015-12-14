package com.limeapi.util;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface ByteFormatted<T extends ByteFormatted<?>> {
    public T fromBytes(byte[][] bytes);

    public byte[][] toBytes();
}
