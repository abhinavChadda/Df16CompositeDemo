package com.dreamforce.demo.composite.serialization;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A Simple Bean that holds the Response of an individual subRequest in Composite API.
 * 
 * @author achadda
 */
public class CompositeOperationResponse {
	private Object body;
	private String referenceId;
	private Map<String, String> httpHeaders;
	private int httpStatusCode;

	private Map<String, Object> bodyMap;
	@SuppressWarnings("unchecked")
	@JsonIgnore
	public Map<String, Object> getBodyAsMap() {
		if (bodyMap == null) {
			if (httpStatusCode >= 400) {
				// Convert list<errors> to a map with the first error info. Not ideal, but works for the Demo.
				bodyMap = ((Map<String, Object>) ((List<Map<String, Object>>)getBody()).get(0));
			}
			else {
				bodyMap = (Map<String, Object>) getBody();
			}
		}
		return bodyMap;
	}
	
	@JsonIgnore
	public void setBodyAsMap(Map<String, Object> body) {
		this.bodyMap = body;
	}
	
	public Object getBody() {
		return this.body;
	}
	
	public void setBody(Object body) {
		this.body = body;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Map<String, String> getHttpHeaders() {
		return httpHeaders;
	}

	public void setHttpHeaders(Map<String, String> httpHeaders) {
		this.httpHeaders = httpHeaders;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int statusCode) {
		this.httpStatusCode = statusCode;
	}
}
