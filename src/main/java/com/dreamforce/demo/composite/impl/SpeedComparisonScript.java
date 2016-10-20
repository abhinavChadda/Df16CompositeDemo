/**
 * 
 */
package com.dreamforce.demo.composite.impl;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

import com.dreamforce.demo.composite.ExecutionScript;
import com.dreamforce.demo.composite.Executor;
import com.dreamforce.demo.composite.SObject;
import com.dreamforce.demo.composite.utils.DemoUtils;

/**
 * This is an implementation of the {@link ExecutionScript} where the speed
 * comparison is done between Traditional API and CompositeAPI for the same set
 * of instructions. An {@link Executor} is given to it to execute the script
 * 
 * @author achadda
 */
public class SpeedComparisonScript extends ExecutionScript<String> {
	private static final String DEPENDENT_TAB = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	private final Executor executor;
	private final String instanceIdentifier;
	
	private final long executionBegin;
	private long lastExecutionTime;
	
	private BlockingQueue<String> queue;
	private final int size;
	
	private int numberCreated = 1; 
	
	/**
	 * @param executor
	 *            - The {@link Executor} that you want the current script to run
	 *            against
	 * @param instanceIdentifier
	 *            - Currently not used in the script.
	 * @param queue
	 *            - the queue in which to place the message which has the
	 *            response information to be displayed
	 * @param size
	 *            - The number of times to execute the script
	 */
	public SpeedComparisonScript(Executor executor, String instanceIdentifier, BlockingQueue<String> queue, int size) {
		this.executor = executor;
		this.instanceIdentifier = instanceIdentifier;
		this.queue = queue;
		
		executionBegin = System.currentTimeMillis();
		lastExecutionTime = executionBegin;
		
		this.size = size;
	}
	
	/**
	 * Makes a call to {@link Executor#beginExecution()} which usually does the login operation.
	 */
	@Override
	public void begin() throws IOException {
		System.out.println("Instance " + instanceIdentifier + " has startedexecution"); // This is not debug statement,I want it to always go to log in the console.
		executor.beginExecution();
		_printMessageWithTiming("Login");
	}
	
	/**
	 * The main method which stores the script and what needs to be done script
	 */
	@Override
	public void executeScript() {
		for (int i = 0; i < size; i++) {
			_createAccountTree(i);
		}
	}
	
	/**
	 * Creates an account tree with one account and three contacts under it.
	 */
	private void _createAccountTree(int index) {
		SObject account1 = new SObject("Account" + index, "Account");
		account1.setField("Name", "Salesforce.com");
		account1.setField("Industry", "Technology");
		account1.setField("Website", "www.Salesforce.com");
		String accountId = _createSobject(account1, 0);
		
		SObject contact = new SObject("Contact" + index, "Contact");
		contact.setField("lastName", "Benioff");
		contact.setField("firstName", "Marc");
		contact.setField("accountId", accountId);
		String contactId = _createSobject(contact, 1);
		
		SObject task = new SObject("Task" + index, "Task");
		task.setField("Subject", "Talk to City Officials about closing the deal");
		task.setField("whatId", accountId);
		task.setField("whoId", contactId);
		_createSobject(task, 2);

		SObject opportunity = new SObject("Opportunity" + index, "Opportunity");
		opportunity.setField("Name", "Salesforce Tower Construction");
		opportunity.setField("accountId", accountId);
		opportunity.setField("stageName", "Prospecting");
		opportunity.setField("closeDate", "2017-04-24T10:39:00.000+0000");
		_createSobject(opportunity, 1);
	}
	
	/**
	 * Create an sObject by calling the {@link Executor#createSobject(SObject)}.
	 * Do the logging for the response.
	 * 
	 * @return the response from {@link Executor#createSobject(SObject)}
	 */
	protected String _createSobject(SObject sObject, int level) {
		try {
			String id = executor.createSobject(sObject);
			if (id == null) {
				_printMessageWithTiming(_getDependentSpaces(level) + "Creation failed");
			} else {
				_printSobjectMessage(sObject, level);
			}
			
			DemoUtils.debug("id Created: " + id);
			return id;
		} catch (IOException e) {
			_printMessageWithTiming("Error in creating " + sObject.getAttributes().get("type"));
		}
		return null;
	}

	/**
	 * Finalize the execution by calling {@link Executor#finalizeExecution()} and print the required information.
	 */
	@Override
	public void finalizeExecution() throws IOException {
		executor.finalizeExecution();
		_printMessageWithTiming("Finalizing Execution");
		_printRaw("TotalTime: " + (System.currentTimeMillis() - executionBegin) + " ms");
		_printRaw("Ids created: " + executor.getIdsCreated());
		_printRaw(DemoUtils.TERMINATION_STRING);
		
		boolean isComposite = executor instanceof CompositeExecutor;
		System.out.println("Instance " + instanceIdentifier + " has completed Execution, Composite=" + isComposite); // This is not debug statement, I want to always log it.
	}
		
	/**
	 * Utility method to print the message for the response of creating an sObject
	 */
	private static final String SOBJECT_MESSAGE = "%s(%s) Created %s"; 
	private void _printSobjectMessage(SObject sObject, int level) {
		_printMessageWithTiming(String.format(SOBJECT_MESSAGE, 
					_getDependentSpaces(level),
					(numberCreated++),
					sObject.getAttributes().get("type")));
	}
	
	/**
	 * Based on the position in the dependent tree, the number of tabs are inserted for the output.
	 */
	private String _getDependentSpaces(int level) {
		StringBuilder builder = new StringBuilder("");
		IntStream.range(0, level).forEach((x) -> builder.append(DEPENDENT_TAB));
		return builder.toString();
	}

	/**
	 * Store the message in the queue so that can be displayed to the UI.
	 */
	private void _printMessageWithTiming(String message) {
		long currentTime = System.currentTimeMillis();
		long timeTaken = currentTime - lastExecutionTime;
		lastExecutionTime = currentTime;
		
		_printRaw(message + ", Time Taken: " + timeTaken + " ms");
	}

	@Override
	protected BlockingQueue<String> getQueue() {
		return queue;
	}

	@Override
	protected String getInstanceIdentifier() {
		return instanceIdentifier;
	}
	
	/**
	 * Goes through the results and returns all the elements in the queue
	 * except {@link DemoUtils#TERMINATION_STRING} as the client code depends on
	 * that to terminate polling. If there is no element available it returns {@link DemoUtils#WAITING_STRING}
	 */
	private static String _returnNonBlockingResponse(BlockingQueue<String> queue) throws InterruptedException {
		String isDone = queue.peek();
		if (DemoUtils.TERMINATION_STRING.equals(isDone)) {
			return queue.take();
		}
		
		StringBuilder builder = new StringBuilder();
		boolean shouldProceed = true;
		while(shouldProceed == true) {
			String nextVal = queue.peek();
			if (nextVal != null) {
				builder.append(queue.take()).append("<br>");
				if (DemoUtils.TERMINATION_STRING.equals(queue.peek())) { // If we are at the end, don't take it.
					shouldProceed = false;
				}
			} else {
				shouldProceed = false; // if we've taken everything, don't proceed.
			}
		}
		if (builder.length() == 0) {
			DemoUtils.debug(DemoUtils.WAITING_STRING);
			return DemoUtils.WAITING_STRING;
		}
		return builder.substring(0, builder.length()-4); // -4 to remove the last <br>
	}
	
	/**
	 * @return the top element in the queue. Blocks till an element is available.
	 */
	private static String _returnBlockingResponse(BlockingQueue<String> queue) throws InterruptedException {
		return queue.take();
	}
	
	/**
	 * Helper method to either return Blocking or Non-Blocking response
	 */
	public static String returnResponse(BlockingQueue<String> queue, boolean blocking) throws InterruptedException {
		if (blocking) {
			return _returnBlockingResponse(queue);
		} else {
			return _returnNonBlockingResponse(queue);
		}
	}
}
