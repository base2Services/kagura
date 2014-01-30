package com.base2.kagura.core.report.parameterTypes;

import java.util.Collection;

/**
 * A parameter with a boolean backing value. PropertyUtils from the Apache Commons Bean library is used to populate
 * this value.
 * User: aubels
 * Date: 17/07/13
 * Time: 2:56 PM
 */
public class SingleParamConfig extends ParamConfig {
    /**
     * Constructor with lots of helpers.
     * {@inheritDoc}
     */
    public SingleParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    /**
     * Protected constructor for deserialization
     */
    protected SingleParamConfig() {
    }

    private String value;

    /**
     * Constructor with lots of helpers, specialized to help with a list of values.
     * {@inheritDoc}
     */
    public SingleParamConfig(String name, String type, Collection list) {
        super(name, type, list);
    }

    /**
     * Gets the value.
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
