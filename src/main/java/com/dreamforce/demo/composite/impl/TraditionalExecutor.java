package com.dreamforce.demo.composite.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.HttpMethod;

import com.dreamforce.demo.composite.Executor;
import com.dreamforce.demo.composite.SObject;
import com.dreamforce.demo.composite.utils.DemoUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.soap.partner.PartnerConnection;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * This is the implementation of the {@link Executor} which makes a call to the
 * Salesforce Server for every request. This is how CRUD operation are made with
 * the API before the existence of Composite API.
 * 
 * @author achadda
 */
public class TraditionalExecutor implements Executor {

	// Client creation is expensive, create only once
	private final Client client = Client.create(new DefaultClientConfig());
	private final ObjectMapper MAPPER = new ObjectMapper();
	
	private String sessionId;
	private List<String> idsCreated = new ArrayList<>(); // Keep a track of all the IDs created, to show in the UI.

	@Override
	public void beginExecution() throws IOException {
		// Do the login call.
		_getSessionId();
	}
	
	@Override
	public String createSobject(SObject object) throws IOException {
		String id = executeAndGetField(
				DemoUtils.getEntityCreateUrl(object), HttpMethod.POST, object,
				object.getAttributes().get("referenceId"), "id");
		if (id == null) {
			idsCreated.add("Failed, Couldn't create " + DemoUtils.extractReferenceId(object));
		} else {
			idsCreated.add(id);
		}
		return id;
	}

	@Override
	public String executeAndGetField(String uri, String method, Object input,
			String referenceId, String field) throws IOException {
		return (String) executeAndGetMap(MAPPER, client, _getSessionId(), uri, method, input).get(field);
	}

	@Override
	public void finalizeExecution() throws IOException {
		// Nothing to do in Traditional executor.
	}

	/**
	 * Make an API call to the Salesforce Servers and get the response of the call as a Map of field values.
	 */
	public static Map<String, Object> executeAndGetMap(ObjectMapper mapper,
			Client client, String sessionId, String uri, String method, Object input) throws IOException {

		ClientResponse response = DemoUtils.executeRemoteCall(mapper, client, sessionId, uri, method, input);

		Map<String, Object> responseMap = new HashMap<>();
		if (response.getStatus() >= 400) {
			// #hackAlert: this isn't the right thing to do. But keeping it this way for simplicity
			responseMap.put("result", 
					mapper.readValue(response.getEntityInputStream(),
							new TypeReference<List<Map<String, Object>>>() {
					}));
		} else {
			responseMap = mapper.readValue(response.getEntityInputStream(),
					new TypeReference<Map<String, Object>>() { });
		}

		if (DemoUtils.isDebuggingSerialization()) {
			// Log the response if we need to.
			DemoUtils.debug("RESPONSE: " + mapper.writeValueAsString(responseMap));
		}

		return responseMap;
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
