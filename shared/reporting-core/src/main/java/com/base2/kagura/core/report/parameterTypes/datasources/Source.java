package com.base2.kagura.core.report.parameterTypes.datasources;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aubels
 *         Date: 17/12/2013
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.WRAPPER_OBJECT,
        defaultImpl = OptionList.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Groovy.class, name = "groovy"),
        @JsonSubTypes.Type(value = SQL.class, name = "sql"),
        @JsonSubTypes.Type(value = OptionList.class, name = "list"),
})
public abstract class Source {
    public abstract Collection<Object> getValues();
    protected Map<String, Object> extra = new HashMap<String, Object>();

    public void prepareParameter(Map<String, Object> extra) {
        this.extra = extra;
    }
}
