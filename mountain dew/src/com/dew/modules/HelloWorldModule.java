package com.dew.modules;

import com.dew.io.ServerIO;
import com.dew.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

@SuppressWarnings("serial")
public class HelloWorldModule extends Module {
	public HelloWorldModule() {
		super("HelloWorldModule");
		addMethod(new ModuleMethod("helloWorld", 0, DataType.VOID) {
			@Override
			public DynamicData executeMethod(DynamicData[] arguments) {
				ServerIO.print("Hello, world!");
				return null;
			}
		}, "Prints \"Hello, world!\".");
	}
}