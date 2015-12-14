package com.dew.modules;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

import com.dew.io.ServerIO;
import com.dew.lang.ArrayLengthException;
import com.dew.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public abstract class Module implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -61597007366328705L;
	protected final String moduleName;
	protected final BigInteger moduleNameBigInteger;
	protected final ArrayList<ModuleMethod> methods = new ArrayList<ModuleMethod>();

	public Module(String moduleName) {
		this.moduleName = moduleName;
		this.moduleNameBigInteger = BinaryOperations
				.bigIntegerFromBytes(BinaryOperations
						.characterArrayToByteArray(moduleName.toCharArray()));
		ModuleManager.addModule(this);
	}

	public Module addMethod(ModuleMethod method) {
		methods.add(method);
		return this;
	}

	public Module addMethod(ModuleMethod method, String desc) {
		method.setMethodDescription(desc);
		methods.add(method);
		return this;
	}

	public DynamicData executeMethod(DynamicData[] arguments,
			BigInteger methodName) {
		try {
			return getMethod(methodName).executeMethod(arguments);
		} catch (ArrayLengthException e) {
			ServerIO.writeException(e);
		}
		return null;
	}

	public DynamicData executeMethod(DynamicData[] arguments, String methodName) {
		try {
			return getMethod(methodName).executeMethod(arguments);
		} catch (ArrayLengthException e) {
			ServerIO.writeException(e);
		}
		return null;
	}

	public ModuleMethod getMethod(BigInteger methodName) {
		for (ModuleMethod method : methods)
			if (method.getMethodNameBigInteger().compareTo(methodName) == 0)
				return method;
		ServerIO.print("[" + this + "] No method found for name: " + methodName);
		return null;
	}

	public ModuleMethod getMethod(String methodName) {
		BigInteger name;
		try {
			name = new BigInteger(methodName, 16);
		} catch (NumberFormatException e) {
			name = BinaryOperations.bigIntegerFromBytes(BinaryOperations
					.characterArrayToByteArray(methodName.toCharArray()));
		}
		return getMethod(name);
	}

	public int getMethodsLength() {
		return methods.size();
	}

	public String getModuleName() {
		return moduleName;
	}

	public BigInteger getModuleNameBigInteger() {
		return moduleNameBigInteger;
	}

	public void listMethods() {
		ServerIO.print("[" + this + "] Method count: " + methods.size());
		ServerIO.print("------------------------------------------------------------");
		for (ModuleMethod m : methods)
			ServerIO.print(m + "          - " + m.getMethodDescription());
		ServerIO.print("------------------------------------------------------------");
	}

	@Override
	public String toString() {
		return moduleName;
	}
}
