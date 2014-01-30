package com.base2.kagura.core.report.parameterTypes;

/**
 * A parameter with a boolean backing value. PropertyUtils from the Apache Commons Bean library is used to populate
 * this value.
 * User: aubels
 * Date: 17/07/13
 * Time: 3:44 PM
 */
public class BooleanParamConfig extends ParamConfig {
    /**
     * Constructor with lots of helpers.
     * {@inheritDoc}
     */
    public BooleanParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    /**
     * Protected constructor for deserialization
     */
    protected BooleanParamConfig() {
    }

    private Boolean value;

    /**
     * Gets the value.
     * @return
     */
    public Boolean getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value
     */
    public void setValue(Boolean value) {
        this.value = value;
    }
}
