package com.base2.kagura.rest.model;

import java.util.List;
import java.util.Map;
import java.io.IOException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author aubels
 *         Date: 4/09/13
 */
@XmlRootElement
public class Parameters {
    Map<String, Object> parameters;

    public Parameters(String json) {
        ObjectMapper mapper = new ObjectMapper();
        parameters = null;
        try
        {
            parameters = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    protected Parameters() {
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
