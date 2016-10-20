/**
 * 
 */
package com.dreamforce.demo.composite;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import com.dreamforce.demo.composite.utils.DemoUtils;

/**
 * The interface defining a basic ExecutionScript
 * 
 * @author achadda
 */
public abstract class ExecutionScript<T> implements Runnable {

	public abstract void begin() throws IOException;
	public abstract void executeScript() throws IOException;
	public abstract void finalizeExecution() throws IOException;
	
	protected abstract BlockingQueue<T> getQueue();
	protected abstract String getInstanceIdentifier();
	
	@Override
	public final void run() {
		try {
			begin();
			executeScript();
			finalizeExecution();
		} catch (IOException e) {
			DemoUtils.debug("Connection Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	protected final void _printRaw(T message) {
		DemoUtils.debug(message);
		try {
			getQueue().put(message);
		} catch (InterruptedException e) {
			System.out.println("FATAL ERROR");
			e.printStackTrace();
		}
	}
}
