package com.dreamforce.demo.composite;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This is the executor interface. Traditional and modern are two children this
 * has.
 * 
 * @author achadda
 */
public interface Executor {
	/**
	 * Begin the execution which usually means login, but can perform other
	 * setup operations.
	 */
	void beginExecution() throws IOException;

	/**
	 * Create an sObject and return its Id. For Traditional Executor, this would
	 * be the salesforce 18-char ID, and for composite executor this would be a
	 * reference Id of the format <code>'@{account1.id}'</code>
	 */
	String createSobject(SObject sObject) throws IOException;

	/**
	 * execute the uri, and get the value for a given field.
	 * 
	 * @throws URISyntaxException
	 */
	String executeAndGetField(String uri, String method, Object input,
			String referenceId, String field) throws IOException;

	/**
	 * To be called at the end of the execution to clean up or perform other
	 * tasks
	 */
	void finalizeExecution() throws IOException;

	/**
	 * @return a formatted String that shows the list of Ids that were created.
	 */
	String getIdsCreated();
}
