package com.base2.kagura.core.report.parameterTypes.datasources;

import java.util.Collection;

/**
 * @author aubels
 *         Date: 17/12/2013
 */
public class OptionList extends Source {
    private Collection<Object> values;

    public Collection<Object> getValues() {
        return values;
    }

    public void setValues(Collection<Object> values) {
        this.values = values;
    }

    public OptionList(Collection<Object> values) {
        this.values = values;
    }

    public OptionList() {

    }
    public OptionList(String in) {

    }
}
