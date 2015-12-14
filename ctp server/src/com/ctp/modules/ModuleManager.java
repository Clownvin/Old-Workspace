package com.ctp.modules;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;

import com.ctp.io.FileManager;
import com.ctp.io.ServerIO;
import com.ctp.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class ModuleManager {
	public static void addModule(Module module) {
		MODULES.add(module);
		ServerIO.print("[ModuleManager] Added module: " + module + ".");
	}

	public static DynamicData executeMethod(BigInteger moduleName,
			BigInteger methodName, DynamicData[] arguments) {
		return getModule(moduleName).executeMethod(arguments, methodName);
	}

	public static DynamicData executeMethod(BigInteger moduleName,
			String methodName, DynamicData[] arguments) {
		return getModule(moduleName).executeMethod(arguments, methodName);
	}

	public static DynamicData executeMethod(String moduleName,
			BigInteger methodName, DynamicData[] arguments) {
		return getModule(moduleName).executeMethod(arguments, methodName);
	}

	public static DynamicData executeMethod(String moduleName,
			String methodName, DynamicData[] arguments) {
		return getModule(moduleName).executeMethod(arguments, methodName);
	}
	
	public static DynamicData executeMethod(BigInteger moduleName,
			BigInteger methodName) {
		return getModule(moduleName).executeMethod(null, methodName);
	}

	public static DynamicData executeMethod(BigInteger moduleName,
			String methodName) {
		return getModule(moduleName).executeMethod(null, methodName);
	}

	public static DynamicData executeMethod(String moduleName,
			BigInteger methodName) {
		return getModule(moduleName).executeMethod(null, methodName);
	}

	public static DynamicData executeMethod(String moduleName,
			String methodName) {
		return getModule(moduleName).executeMethod(null, methodName);
	}

	public static Module getModule(BigInteger moduleName) {
		for (Module module : MODULES) {
			if (module.getModuleNameBigInteger().compareTo(moduleName) == 0)
				return module;
		}
		ServerIO.print("[ModuleManager] No module found for name: 0x"
				+ moduleName.toString(16));
		return null;
	}

	public static Module getModule(String moduleName) {
		BigInteger name;
		try {
			name = new BigInteger(moduleName, 16);
		} catch (NumberFormatException e) {
			name = BinaryOperations.bigIntegerFromBytes(BinaryOperations
					.characterArrayToByteArray(moduleName.toCharArray()));
		}
		return getModule(name);
	}

	public static void listModules() {
		ServerIO.print("[ModuleManager] Active modules: " + MODULES.size());
		ServerIO.print("-------------------------------");
		for (Module m : MODULES)
			ServerIO.print(m.moduleName);
		ServerIO.print("-------------------------------");
	}
	
	public static void listModuleMethods(String name) {
		getModule(name).listMethods();
	}

	public static void loadModules() {
		FileManager.checkFilePath(FileManager.MODULE_PATH);
		File folder = new File(FileManager.MODULE_PATH);
		File[] modules = folder.listFiles();
		if (modules.length == 0) {
			ServerIO.print("[ModuleManager] No modules found.");
			return;
		}
		for (File file : modules) {
			try {
				ObjectInputStream reader = new ObjectInputStream(
						new FileInputStream(file));
				addModule((Module) reader.readObject());
				reader.close();
			} catch (IOException | ClassNotFoundException e) {
				ServerIO.print("[ModuleManager] Encountered problem while loading module: "
						+ file.getPath() + ".");
				ServerIO.writeException(e);
			}
		}
		ServerIO.print("[ModuleManager] Finished loading modules.");
	}

	public static void saveModules() {
		FileManager.checkFilePath(FileManager.MODULE_PATH);
		for (Module m : MODULES) {
			try {
				ObjectOutputStream writer = new ObjectOutputStream(
						new FileOutputStream(FileManager.MODULE_PATH
								+ m.getModuleName()
								+ FileManager.MODULE_FILE_EXTENSION));
				writer.writeObject(m);
				writer.close();
			} catch (IOException e) {
				ServerIO.print("[ModuleManager] There was an error saving module: "
						+ FileManager.MODULE_PATH
						+ m.getModuleName()
						+ FileManager.MODULE_FILE_EXTENSION);
				ServerIO.writeException(e);
			}
		}
		ServerIO.print("[ModuleManager] Finished saving modules.");
	}

	private static final ArrayList<Module> MODULES = new ArrayList<Module>();
	
	private static final ModuleManager SINGLETON = new ModuleManager();
	
	static {
		ServerIO.print("[ModuleManager] ModuleManager is up and running.");
	}

	private ModuleManager() {
		// To prevent instantiation.
	}

	public static ModuleManager getSingleton() {
		return SINGLETON;
	}
}
