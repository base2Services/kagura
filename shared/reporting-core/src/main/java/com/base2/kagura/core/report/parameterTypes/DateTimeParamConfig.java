package com.base2.kagura.core.report.parameterTypes;

import java.util.Date;

/**
 * A parameter with a date backing value. PropertyUtils from the Apache Commons Bean library is used to populate
 * this value. Date AND date time exist, as the type reported to Kagura.js are different. There is no other reason
 * at the moment. It could probably be parametrized as a "pattern"
 * User: aubels
 * Date: 17/07/13
 * Time: 3:44 PM
 */
public class DateTimeParamConfig extends ParamConfig {
    /**
     * Constructor with lots of helpers.
     * {@inheritDoc}
     */
    public DateTimeParamConfig(String name, String type, String help, String placeholder) {
        super(name, type, help, placeholder);
    }

    /**
     * Protected constructor for deserialization
     */
    protected DateTimeParamConfig() {
    }

    private Date value;

    /**
     * Gets the value.
     * @return
     */
    public Date getValue() {
        return value;
    }

    /**
     * Sets the value
     * @param value
     */
    public void setValue(Date value) {
        this.value = value;
    }
}
