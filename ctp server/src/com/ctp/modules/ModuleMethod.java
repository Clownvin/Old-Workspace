package com.ctp.modules;

import java.io.Serializable;
import java.math.BigInteger;

import com.ctp.io.ServerIO;
import com.ctp.server.ArrayLengthException;
import com.ctp.util.BinaryOperations;
import com.ctp.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public abstract class ModuleMethod implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -148328801872691989L;
	protected final String methodName;
	protected String methodDescription = "None provided.";
	protected final BigInteger methodNameBigInteger;
	protected final int argumentLength;
	protected final DataType returnType;

	public ModuleMethod(String methodName, int argumentLength,
			DataType returnType) {
		this.methodNameBigInteger = BinaryOperations
				.bigIntegerFromBytes(BinaryOperations
						.characterArrayToByteArray(methodName.toCharArray()));
		this.methodName = methodName;
		this.argumentLength = argumentLength;
		this.returnType = returnType;
	}
	
	public ModuleMethod(String methodName, int argumentLength,
			DataType returnType, String methodDescription) {
		this.methodNameBigInteger = BinaryOperations
				.bigIntegerFromBytes(BinaryOperations
						.characterArrayToByteArray(methodName.toCharArray()));
		this.methodName = methodName;
		this.argumentLength = argumentLength;
		this.returnType = returnType;
		this.methodDescription = methodDescription;
	}

	public DynamicData executeMethod(DynamicData[] arguments)
			throws ArrayLengthException {
		if (arguments.length != argumentLength)
			throw new ArrayLengthException(
					"Arguments length must be equal to specified arguments length.");
		ServerIO.print("\"executeMethod\" of method " + this
				+ " has not been overriden.");
		return null;
	}

	public int getArgumentLength() {
		return argumentLength;
	}
	
	public String getMethodDescription() {
		return methodDescription;
	}

	public String getMethodName() {
		return methodName;
	}

	public BigInteger getMethodNameBigInteger() {
		return methodNameBigInteger;
	}

	public DataType getReturnType() {
		return returnType;
	}
	
	public ModuleMethod setMethodDescription(String desc) {
		this.methodDescription = desc;
		return this;
	}

	@Override
	public String toString() {
		return methodName;
	}
}
