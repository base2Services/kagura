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
package com.base2.kagura.core.report.parameterTypes;

import com.base2.kagura.core.report.parameterTypes.datasources.OptionList;
import com.base2.kagura.core.report.parameterTypes.datasources.Source;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Base type for the a parameter type. Also provides jackson loading information.
 * User: aubels
 * Date: 16/07/13
 * Time: 5:26 PM
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = SingleParamConfig.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MultiParamConfig.class, name = "ManyCombo"),
        @JsonSubTypes.Type(value = SingleParamConfig.class, name = ""),
        @JsonSubTypes.Type(value = BooleanParamConfig.class, name = "Boolean"),
        @JsonSubTypes.Type(value = DateParamConfig.class, name = "Date"),
        @JsonSubTypes.Type(value = DateTimeParamConfig.class, name = "DateTime"),
})
public abstract class ParamConfig {
    String name;
    String type;
    String help;
    String placeholder;
    private Source from;
    private String id;

    /**
     * Constructor
     */
    public ParamConfig() {
    }

    /**
     * Simple constructor. Pre-populates data source with an empty OptionList.
     * @param name
     * @param type
     * @param help
     * @param placeholder
     */
    public ParamConfig(String name, String type, String help, String placeholder) {
        this.name = name;
        this.type = type;
        this.help = help;
        this.placeholder = placeholder;
        this.from = new OptionList( Arrays.asList(new Object[0]));
    }

    /**
     * Simple constructor. Populates data source with values.
     * @param name
     * @param type
     * @param values
     */
    public ParamConfig(String name, String type, Collection values) {
        this.name = name;
        this.type = type;
        this.from = new OptionList(values);
        this.help = "";
        this.placeholder = "";
    }

    /**
     * String constructor
     * @param name
     * @param help
     * @param placeholder
     * @return
     */
    public static ParamConfig String(String name, String help, String placeholder) {
        return new SingleParamConfig(name, "String", help, placeholder);
    }

    /**
     * String constructor
     * @param name
     * @return
     */
    public static ParamConfig String(String name) {
        return String(name, "","");
    }

    /**
     * Combo constructor
     * @param name
     * @return
     */
    public static ParamConfig Combo(String name, Collection list) {
        return new SingleParamConfig(name, "Combo", list);
    }

    /**
     * ManyCombo constructor
     * @param name
     * @return
     */
    public static ParamConfig ManyCombo(String name, Collection list) {
        return new MultiParamConfig(name, "ManyCombo", list);
    }

    /**
     * Number constructor
     * @param name
     * @return
     */
    public static ParamConfig Number(String name) {
        return new SingleParamConfig(name, "Number","","");
    }

    /**
     * Boolean constructor
     * @param name
     * @return
     */
    public static ParamConfig Boolean(String name) {
        return new BooleanParamConfig(name, "Boolean","","");
    }

    /**
     * DateTime constructor
     * @param name
     * @return
     */
    public static ParamConfig DateTime(String name) {
        return new DateTimeParamConfig(name, "DateTime","","");
    }

    /**
     * Date constructor
     * @param name
     * @return
     */
    public static ParamConfig Date(String name) {
        return new DateTimeParamConfig(name, "Date","","");
    }

    /**
     * Parameter display name.
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
     * The type of the parameter. Has to match one of the class names because of Jackson mapping.
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @see #getType()
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Help text. Kagura.js in Javascript example shows this under the input, where appropriate, in italics
     * @return
     */
    public String getHelp() {
        return help;
    }

    /**
     * @see #getHelp()
     * @param help
     */
    public void setHelp(String help) {
        this.help = help;
    }

    /**
     * Place holder, when applicable. This is the grayed out italics text which appears inside the empty text boxes
     * @return
     */
    public String getPlaceholder() {
        return placeholder;
    }

    /**
     * @see #getPlaceholder()
     * @param placeholder
     */
    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "ParamConfig{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", help='" + help + '\'' +
                ", placeholder='" + placeholder + '\'' +
                '}';
    }

    /**
     * Gets the possible values accepted from the data sources, used in Combo boxes and ManyCombos (MultiParamConfig)
     * @return
     */
    public Collection<Object> getValues() {
        if (from != null)
        {
            return from.getValues();
        }
        return null;
//        return Arrays.asList();
    }

    /**
     * Prepares the parameter's datasource, passing it the extra options and if necessary executing the appropriate
     * code and caching the value.
     * @param extra
     */
    public void prepareParameter(Map<String, Object> extra) {
        if (from != null)
        {
            from.prepareParameter(extra);
        }
    }

    /**
     * Ignores values set.
     * @param values
     */
    public void setValues(Collection<Object> values)
    {
        // Ignore..
    }

    /**
     * The parameter ID. Used by FreeMarker to reference the parameter. If ID hasn't been specified it bases it on the
     * name by removing all non-"word" characters (it only allows a-zA-Z.) So:
     * Tom's Param-
     * Becomes
     * TomsParam
     * @return
     */
    public String getId() {
        Pattern replace = Pattern.compile("\\W+");
        return StringUtils.defaultIfEmpty(id, replace.matcher(getName()).replaceAll(""));
    }

    /**
     * @see #getId()
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets the from data source. No getter to prevent it being serialized to the client / middleware.
     * @param from
     */
    public void setFrom(Source from) {
        this.from = from;
    }
}
