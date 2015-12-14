package com.dew.threading;

/**
 * 
 * @author Calvin Gene Hall
 *
 */
public interface ThreadTask {
	public void doTask();

	default void end() {
	}

	default boolean reachedEnd() {
		return true;
	}
	
	default int getPriority() {
		return Thread.NORM_PRIORITY;
	}
}
