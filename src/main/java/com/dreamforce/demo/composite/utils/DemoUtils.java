/**
 * 
 */
package com.dreamforce.demo.composite.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.dreamforce.demo.composite.SObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

/**
 * Bunch of common utilities for running the demo.
 * 
 * @author achadda
 */
public class DemoUtils {
	private DemoUtils() { } // Ensure Static access only.
	
	public static String TERMINATION_STRING = "Done";
	public static String WAITING_STRING = "Wait";
	
	private static ThreadLocal<String> baseServerUrl = new ThreadLocal<>();
	public static void setBaseUrl(String serverUrl) {
		baseServerUrl.set(serverUrl);
	}
	
	/**
	 * @return the app server's base url.
	 */
	public static String getBaseUrl() {
		return baseServerUrl.get();
	}

	private static String _getUserName() {
		return System.getenv("USERNAME");
	}
	private static String _getPassword() {
		return System.getenv("PASSWORD");
	}

	/**
	 * @return true if the system is currently running in debugging mode.
	 */
	public static boolean isDebugging() {
		return Boolean.valueOf(System.getenv().get("ISDEBUGGING"));
	}
	
	/**
	 * @return true if the system is currently running in debugging mode.
	 */
	public static boolean isDebuggingSerialization() {
		return Boolean.valueOf(System.getenv().get("ISDEBUGGINGSERIALIZATION"));
	}
	
	/**
	 * Print a debugging message to the console if debugging is enabled. calls
	 * toString on the object passed in.
	 */
	public static void debug(Object message) {
		if (isDebugging()) {
			System.out.println(message);
		}
	}
	
	/**
	 * @return the base sObjects URl
	 */
	public static String getSObjectsUrl() {
		return "/services/data/v37.0/sobjects/";
	}
	
	/**
	 * @return the url for creating an sObject
	 */
	public static String getEntityCreateUrl(SObject sObject) {
		return getSObjectsUrl() + sObject.getAttributes().get(SObject.TYPE);
	}
		
	/** 
	 * @return the referenceId that is extracted from the {@link SObject}
	 */
	public static String extractReferenceId(SObject sObject) {
		return sObject.getAttributes().get(SObject.REF_ID);
	}

	/**
	 * login and return the session Id to be used. Also set the baseUrl thread local.
	 */
	public static String login() throws IOException {
		ConnectorConfig config = ConnectorConfig.DEFAULT;
		config.setManualLogin(true);
		config.setServiceEndpoint("https://login.salesforce.com/services/Soap/u/37.0"); 

		try {
			PartnerConnection connection = new PartnerConnection(config);
			LoginResult result = connection.login(_getUserName(), _getPassword());
			setBaseUrl(result.getServerUrl().split("/services")[0]);
			return result.getSessionId();
		} catch (ConnectionException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
	
	/**
	 * This is used to build a {@link ClientRequest} and execute against the client that has been passed in. 
	 * @return The raw {@link ClientResponse} that comes back from the API call.
	 */
	public static ClientResponse executeRemoteCall(ObjectMapper mapper, Client client, String sessionId, String uri, String method, Object input) throws IOException {
		ClientRequest clientRequest = buildClientRequest(mapper, sessionId, uri, method, input);
		return client.handle(clientRequest);
	}

	/**
	 * Build a {@link ClientRequest} that you can execute on. Use this to pass it to {@link Client}. It takes care of serializing your object.
	 */
	public static ClientRequest buildClientRequest(ObjectMapper mapper, String sessionId, String uri, String method, Object object) throws IOException {
		URI finalUri;
		try {
			finalUri = new URI(getBaseUrl() + uri);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		ClientRequest.Builder request = new ClientRequest.Builder();
		request.header("Authorization", "Bearer " + sessionId);
		
		if (object != null) {
			request.type(MediaType.APPLICATION_JSON);
			request.entity(new ByteArrayInputStream(mapper.writeValueAsBytes(object)));
			
			if (DemoUtils.isDebuggingSerialization()) { // writeValueAsString is expensive, don't call unless needed
				DemoUtils.debug("REQUEST: " + mapper.writeValueAsString(object)); 
			}
		}
		return request.build(finalUri, method);
	}
	
	public static String getIdsCreated(List<String> ids) {
		StringBuilder builder = new StringBuilder();
		ids.forEach((id) -> {
			if (id.length() == 15 || id.length() == 18) {
				builder.append(createHyperlink(id, true)).append(", ");
			} else {
				builder.append("<b style=\"color:red\">").append(id).append("</b>, ");
			}
		});
		return builder.toString();
	}
	
	private static final String urlFormat = "<a target=\"_blank\" href=%s/%s>%s";
	public static String createHyperlink(String id, boolean closingTag) {
		return String.format(urlFormat, getBaseUrl(), id, id) + (closingTag ? "</a>" : "");
	}
	
	/**
	 * @return The reference syntax where you create a reference from the parent referenceId, and a field in it.
	 */
	public static String generateReferenceId(String referenceId, String field) {
		return "@{" + referenceId + "." + field + "}";
	}
}
