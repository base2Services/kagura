package com.base2.kagura.core.report.parameterTypes;

import java.util.Collection;
import java.util.List;

/**
 * A parameter with a list of strings as the backing value. Allows for selection of multiple values from a combobox.
 * PropertyUtils from the Apache Commons Bean library is used to populate this value.
 * * User: aubels
 * Date: 17/07/13
 * Time: 2:55 PM
 */
public class MultiParamConfig extends ParamConfig {
    /**
     * Constructor with lots of helpers.
     * {@inheritDoc}
     */
    public MultiParamConfig(String name, String type, Collection list) {
        super(name, type, list);
    }

    /**
     * Protected constructor for deserialization
     */
    protected MultiParamConfig() {
    }

    List<String> value;

    /**
     * Gets the value.
     * @return
     */
    public List<String> getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value
     */
    public void setValue(List<String> value) {
        this.value = value;
    }
}
