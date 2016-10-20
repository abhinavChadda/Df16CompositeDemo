/**
 * 
 */
package com.dreamforce.demo.composite;

import java.util.HashMap;
import java.util.Map;

import com.dreamforce.demo.composite.serialization.SObjectRowJsonSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * The representation of an SObject in the demo. It's essentially a map of
 * fieldName to fieldValue. It adds the additional "Attributes" map to the list
 * of fields which helps Salesforce understand the object type
 * 
 * @author achadda
 */
@JsonSerialize(using=SObjectRowJsonSerializer.class)
public class SObject {

	public static final String REF_ID = "referenceId";
	public static final String TYPE = "type";
	
	private final String ATTRIBUTES = "attributes";
	private Map<String, Object> fields = new HashMap<>();
	private Map<String, String> attributesMap = new HashMap<>();
	
	private String type;
	private String referenceId;
	
	public SObject(String referenceId, String type) {
		this.type = type;
		this.referenceId = referenceId;
	}
	
	public SObject() {
		
	}
	
	public void setField(String fieldName, Object value) {
		this.fields.put(fieldName, value);
	}
	
	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
		if (fields.containsKey(ATTRIBUTES)) {
			@SuppressWarnings("unchecked")
			Map<String, Object> attributes = (Map<String, Object>) fields.get(ATTRIBUTES);
			this.referenceId = (String) attributes.getOrDefault(REF_ID, null);
			this.type = (String) attributes.getOrDefault(TYPE, null);
		}
	}
	
	public Map<String, Object> getFields() {
		return this.fields;
	}
	
	@JsonIgnore
	public Map<String, String> getAttributes() {
		if (attributesMap.size() == 0) {
			attributesMap.put(TYPE, type);
			attributesMap.put(REF_ID, referenceId);
		}
		return attributesMap;
	}
}