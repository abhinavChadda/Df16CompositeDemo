/**
 * 
 */
package com.dreamforce.demo.composite.serialization;

import java.util.List;

/**
 * A simple bean that holds a {@link List} of {@link CompositeOperation}. This
 * is forms the input to the Composite API on Salesforce
 * 
 * @author achadda
 */
public class CompositeRequest {

	private boolean allOrNone = true;
	private List<CompositeOperation> compositeRequest;

	public List<CompositeOperation> getCompositeRequest() {
		return compositeRequest;
	}

	public void setCompositeRequest(List<CompositeOperation> compositeRequest) {
		this.compositeRequest = compositeRequest;
	}
	
	public void setAllOrNone(boolean allOrNone) {
		this.allOrNone = allOrNone;
	}
	
	public boolean getAllOrNone() {
		return this.allOrNone;
	}
}
