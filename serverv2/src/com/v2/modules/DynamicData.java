package com.v2.modules;

import java.io.Serializable;

import com.v2.lang.InvalidTypeException;
import com.v2.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public abstract class DynamicData implements Serializable {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1965509886885232575L;
    protected DataType dataType = DataType.UNKNOWN;
    protected Object data = null;
    protected boolean array = false;

    public DynamicData(DataType dataType, Object data) {
	this.dataType = dataType;
	this.data = data;
    }

    public DynamicData(DataType dataType, Object data, boolean array) {
	this.dataType = dataType;
	this.data = data;
	this.array = array;
    }

    public byte getByte() {
	if (dataType == DataType.BYTE)
	    return (byte) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type Byte. Specified type: " + dataType
		    + ".");
    }

    public byte[] getBytes() {
	if (dataType == DataType.BYTE && isArray())
	    return (byte[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type Byte, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public Object getData() {
	return data;
    }

    public DataType getDataType() {
	return dataType;
    }

    public double getDouble() {
	if (dataType == DataType.DOUBLE)
	    return (double) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type Double. Specified type: " + dataType
		    + ".");
    }

    public double[] getDoubles() {
	if (dataType == DataType.DOUBLE && isArray())
	    return (double[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type Double, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public float getFloat() {
	if (dataType == DataType.FLOAT)
	    return (float) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type Float. Specified type: " + dataType
		    + ".");
    }

    public float[] getFloats() {
	if (dataType == DataType.FLOAT && isArray())
	    return (float[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type Float, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public int getInteger() {
	if (dataType == DataType.INT)
	    return (int) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type Integer. Specified type: " + dataType
		    + ".");
    }

    public int[] getIntegers() {
	if (dataType == DataType.INT && isArray())
	    return (int[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type Integer, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public long getLong() {
	if (dataType == DataType.LONG)
	    return (long) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type Long. Specified type: " + dataType
		    + ".");
    }

    public long[] getLongs() {
	if (dataType == DataType.LONG && isArray())
	    return (long[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type Long, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public String getString() {
	if (dataType == DataType.STRING)
	    return (String) data;
	else
	    throw new InvalidTypeException("Data \"" + data
		    + "\" is not of type String. Specified type: " + dataType
		    + ".");
    }

    public String[] getStrings() {
	if (dataType == DataType.STRING && isArray())
	    return (String[]) data;
	else
	    throw new InvalidTypeException(
		    "Data \""
			    + data
			    + "\" is not of type String, and/or is not an array. Specified type: "
			    + dataType + ", is array: " + isArray() + ".");
    }

    public boolean isArray() {
	return array;
    }

    public DynamicData setArray(boolean array) {
	this.array = array;
	return this;
    }

    public DynamicData setData(Object data) {
	this.data = data;
	return this;
    }

    public DynamicData setData(Object data, DataType dataType) {
	this.data = data;
	this.dataType = dataType;
	return this;
    }

    public DynamicData setData(Object data, DataType dataType, boolean array) {
	this.data = data;
	this.dataType = dataType;
	this.array = array;
	return this;
    }

    public DynamicData setDataType(DataType dataType) {
	this.dataType = dataType;
	return this;
    }
}
