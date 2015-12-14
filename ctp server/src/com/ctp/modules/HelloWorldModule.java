package com.ctp.modules;

import com.ctp.io.ServerIO;
import com.ctp.util.DataType;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

@SuppressWarnings("serial")
public class HelloWorldModule extends Module {
	public HelloWorldModule() {
		super("HelloWorldModule");
		addMethod(new ModuleMethod("HelloWorld", 0, DataType.VOID) {
			@Override
			public DynamicData executeMethod(DynamicData[] arguments) {
				ServerIO.print("Hello, world!");
				return null;
			}
		}, "Prints \"Hello, world!\".");
	}
}