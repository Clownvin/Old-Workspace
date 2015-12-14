package com.v2.modules;

import com.v2.io.ServerIO;
import com.v2.util.DataType;

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