package com.ctp.modules;

import java.io.Serializable;

import com.ctp.util.DataType;

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

	public Object getData() {
		return data;
	}

	public DataType getDataType() {
		return dataType;
	}
	
	public String getString() {
		if (dataType == DataType.STRING)
			return (String) data;
		else
			return null;
	}
	
	public byte getByte() {
		if (dataType == DataType.BYTE)
			return (byte) data;
		else
			return -1; // Throw exception
	}
	
	public int getInteger() {
		if (dataType == DataType.INT)
			return (int) data;
		else
			return -1; // Throw exception
	}
	
	public float getFloat() {
		if (dataType == DataType.FLOAT)
			return (float) data;
		else
			return -1; // Throw exception
	}
	
	public long getLong() {
		if (dataType == DataType.LONG)
			return (long) data;
		else
			return -1; // Throw exception
	}
	
	public double getDouble() {
		if (dataType == DataType.DOUBLE)
			return (double) data;
		else
			return -1; // Throw exception
	}
	
	public String[] getStrings() {
		if (dataType == DataType.STRING && isArray())
			return (String[]) data;
		else
			return null;
	}
	
	public byte[] getBytes() {
		if (dataType == DataType.BYTE && isArray())
			return (byte[]) data;
		else
			return null; // Throw exception
	}
	
	public int[] getIntegers() {
		if (dataType == DataType.INT && isArray())
			return (int[]) data;
		else
			return null; // Throw exception
	}
	
	public float[] getFloats() {
		if (dataType == DataType.FLOAT && isArray())
			return (float[]) data;
		else
			return null; // Throw exception
	}
	
	public long[] getLongs() {
		if (dataType == DataType.LONG && isArray())
			return (long[]) data;
		else
			return null; // Throw exception
	}
	
	public double[] getDoubles() {
		if (dataType == DataType.DOUBLE && isArray())
			return (double[]) data;
		else
			return null; // Throw exception
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
