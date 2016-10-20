/**
 * 
 */
package com.dreamforce.demo.composite.serialization;

/**
 * A simple Bean to hold an individual subRequest in Composite API. 
 * 
 * @author achadda
 */
public class CompositeOperation {

	private String method;
	private String referenceId;
	private Object body;
	private String url;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public Object getBody() {
		return body;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
