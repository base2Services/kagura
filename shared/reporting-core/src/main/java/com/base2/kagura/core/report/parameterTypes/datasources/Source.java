/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.base2.kagura.core.report.parameterTypes.datasources;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for combo box data sources. Also contains the Jackson mapping details.
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
    /**
     * Abstract class for retrieving the values. Depends on how the backend work, in some cases it's just a getter
     * in others it runs. When Jackson serializes the entity it doesn't need to know.
     * @return
     */
    public abstract Collection<Object> getValues();

    /**
     * Extra options, passed via prepareParameters().
     */
    protected Map<String, Object> extra = new HashMap<String, Object>();

    /**
     * Executes scripts in some cases, and stores or applies the extra options. Extra options in this case are sourced
     * from the middleware. This is executed by the ReportConnector's prepareParameters() call.
     * @param extra
     */
    public void prepareParameter(Map<String, Object> extra) {
        this.extra = extra;
    }
}
