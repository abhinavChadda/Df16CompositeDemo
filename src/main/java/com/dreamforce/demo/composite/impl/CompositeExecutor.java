/**
 * 
 */
package com.dreamforce.demo.composite.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.HttpMethod;

import com.dreamforce.demo.composite.Executor;
import com.dreamforce.demo.composite.SObject;
import com.dreamforce.demo.composite.serialization.CompositeOperation;
import com.dreamforce.demo.composite.serialization.CompositeRequest;
import com.dreamforce.demo.composite.serialization.CompositeResponse;
import com.dreamforce.demo.composite.utils.DemoUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.soap.partner.PartnerConnection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * An {@link Executor} that uses Composite API to relate calls together and
 * creates a single round trip to salesforce's server. It creates in memory
 * representations of the objects till {@link #finalizeExecution()} when the
 * actual call is made.
 * 
 * @author achadda
 */
public class CompositeExecutor implements Executor {

	// Client creation is expensive, create only once per execution.
	private final Client client = Client.create(new DefaultClientConfig());
	private final ObjectMapper objectMapper = new ObjectMapper();
	
	private String sessionId;
	private List<String> idsCreated = new ArrayList<>(); // Keep a list of the Ids that are created to be displayed in the UI.

	private List<CompositeOperation> compositeOperations = new ArrayList<>();

	@Override
	public void beginExecution() throws IOException {
		// login.
		_getSessionId();
	}

	@Override
	public String createSobject(SObject sObject) throws IOException {
		return executeAndGetField(
				DemoUtils.getEntityCreateUrl(sObject),
					HttpMethod.POST, sObject,
					DemoUtils.extractReferenceId(sObject), "id");
	}

	@Override
	public String executeAndGetField(String uri, String method, Object input, String referenceId, String field) throws IOException {
		// Create an in-memory representation of the API call. This will be sent to the Composite API at #finalizeExecution 
		CompositeOperation operation = new CompositeOperation();
		operation.setBody(input);
		operation.setMethod(method);
		operation.setUrl(uri);
		operation.setReferenceId(referenceId);
		compositeOperations.add(operation);

		return DemoUtils.generateReferenceId(referenceId, field);
	}

	@Override
	public void finalizeExecution() throws IOException {
		// Now make the actual call to Composite API with the requests that have been collected so far.
		CompositeRequest request = new CompositeRequest();
		request.setCompositeRequest(compositeOperations);

		ClientResponse response = DemoUtils.executeRemoteCall(objectMapper, client,
				_getSessionId(), "/services/data/v38.0/composite",
				HttpMethod.POST, request);
		
		// Read the response as CompositeRespose.java/
		CompositeResponse compositeResponse = objectMapper.readValue(
				response.getEntityInputStream(), CompositeResponse.class);
		
		// Iterate through the results that came out of the call to Composite API
		compositeResponse.getCompositeResponse().forEach((res) -> {
			if (res.getHttpStatusCode() >= 400) {
				DemoUtils.debug(res.getReferenceId() + ", failed");
				idsCreated.add("Failed, Rolled back " + res.getReferenceId());
			} else {
				idsCreated.add((String) res.getBodyAsMap().get("id"));
			}
		});
	}

	/**
	 * @return the sessionId to be used in the REST call. If sessionId is not
	 *         present, it makes a login call through {@link PartnerConnection}
	 */
	private String _getSessionId() throws IOException {
		if (this.sessionId == null) {
			this.sessionId = DemoUtils.login();
		}

		return this.sessionId;
	}

	@Override
	public String getIdsCreated() {
		return DemoUtils.getIdsCreated(idsCreated);
	}
}
 