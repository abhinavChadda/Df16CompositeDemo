/**
 * 
 */
package com.dreamforce.demo.composite.serialization;

import java.util.List;

/**
 * This is s Simple Bean that holds the response from the call to Salesforce's
 * Composite API. It stores a list of {@link CompositeOperationResponse} which
 * contains the response for each of the subRequest.
 * 
 * @author achadda
 */
public class CompositeResponse {
	private List<CompositeOperationResponse> compositeResponse;

	public List<CompositeOperationResponse> getCompositeResponse() {
		return compositeResponse;
	}

	public void setCompositeResponse(List<CompositeOperationResponse> compositeResponse) {
		this.compositeResponse = compositeResponse;
	}
	
}
