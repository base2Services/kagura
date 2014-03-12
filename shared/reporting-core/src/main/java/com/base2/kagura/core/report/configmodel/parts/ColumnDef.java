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
package com.base2.kagura.core.report.configmodel.parts;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Column definition, used by Kagura.js, defined in report configuration file.
 * User: aubels
 * Date: 16/07/13
 * Time: 5:49 PM
 * This defines what a column definition looks like, and what it's called.
 * It has no impact in terms of the reporting-core. However it is used in kagura.js and the report configuration.
 */
public class ColumnDef {
    private String name;
    private String label;
    private Map<String, Object> extraOptions;

    /** Default constructor */
    public ColumnDef() {
        this.extraOptions = new HashMap<String, Object>();
    }

    /**
     * Column constructor. Pre-populates "styleType" of "extraOptions"
     * @param name The name of the column, set "label" to change the displayed value.
     * @param styleType The style type. Passed via extraOptions to kagura.js, allows for special display types
     */
    public ColumnDef(String name, String styleType) {
        this.name = name;
        this.extraOptions = new HashMap<String, Object>();
        extraOptions.put("styleType", styleType);
    }

    /**
     * Name of the column, name to match SQL column against.
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @see #getName()
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Normal constructor, pre-populates "extraOptions's styleType" with the type "numbers"
     * @param name the name of the column
     * @return an initialized version
     */
    public static ColumnDef Number(String name) {
        return new ColumnDef(name, "numbers");
    }

    /**
     * Normal constructor, pre-populates "extraOptions's styleType" with the type "numbers"
     * @param name the name of the column
     * @return an initialized version
     */
    public static ColumnDef Text(String name) {
        return new ColumnDef(name, "text");
    }

    /**
     * Gets the display label, if no label has been set, uses the "name" field.
     * @return
     */
    public String getLabel() {
        return StringUtils.defaultString(label,name);
    }

    /**
     * @see #getLabel()
     * @param label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Extra options. Passed verbatim to kagura.js from report configuration. Put custom configuration here.
     * @return Extra options.
     */
    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }

    /**
     * @see #getExtraOptions()
     * @param extraOptions
     */
    public void setExtraOptions(Map<String, Object> extraOptions) {
        this.extraOptions = extraOptions;
    }
}
