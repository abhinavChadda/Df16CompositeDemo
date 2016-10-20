/**
 * 
 */
package com.dreamforce.demo.composite.serialization;

import java.io.IOException;
import java.util.Map;

import com.dreamforce.demo.composite.SObject;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * A simple JSON Serializer for SObjects. It writes the 'attributes' map along with the other fields.
 *  
 * @author achadda
 */
public class SObjectRowJsonSerializer extends JsonSerializer<SObject> {

	@Override
	public void serialize(SObject value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException { 
		
		jgen.writeStartObject();
		jgen.writeObjectField("attributes", value.getAttributes());
		for (Map.Entry<String, Object> entry : value.getFields().entrySet()) {
			jgen.writeObjectField(entry.getKey(), entry.getValue());
		}
		
		jgen.writeEndObject();
	}

}
