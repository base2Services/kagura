package com.base2.kagura.core.report.parameterTypes.datasources;

import java.util.Collection;

/**
 * A combo / manycombo value list, which is a just a plain old list intialized by jackson / yaml.
 * @author aubels
 *         Date: 17/12/2013
 */
public class OptionList extends Source {
    private Collection<Object> values;

    /**
     * Returns the values..
     * @return a list of values
     */
    public Collection<Object> getValues() {
        return values;
    }

    /**
     * {@inheritDoc}
     * @param values
     */
    public void setValues(Collection<Object> values) {
        this.values = values;
    }

    /**
     * Constructor, with value / options.
     * @param values
     */
    public OptionList(Collection<Object> values) {
        this.values = values;
    }

    /**
     * Default constructor.
     */
    public OptionList() {

    }
    /**
     * Reduces parsing errors. Should be avoided. (Should be an error condition.)
     */
    public OptionList(String in) {

    }
}
