package com.base2.kagura.core.report.parameterTypes;

import com.base2.kagura.core.report.configmodel.ReportConfig;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 16/07/13
 * Time: 5:26 PM
 * To change this template use File | Settings | File Templates.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        defaultImpl = SingleParamConfig.class,
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SqlParamConfig.class, name = "SQL"),
        @JsonSubTypes.Type(value = MultiParamConfig.class, name = "ManyCombo"),
        @JsonSubTypes.Type(value = SingleParamConfig.class, name = ""),
        @JsonSubTypes.Type(value = BooleanParamConfig.class, name = "Boolean"),
        @JsonSubTypes.Type(value = DateParamConfig.class, name = "Date"),
        @JsonSubTypes.Type(value = DateTimeParamConfig.class, name = "DateTime"),
        @JsonSubTypes.Type(value = GroovyParamConfig.class, name = "Groovy"),
})
public abstract class ParamConfig {
    String name;
    String type;
    String help;
    String placeholder;
    private Collection<Object> values;
    private String id;

    public ParamConfig() {
    }

    public ParamConfig(String name, String type, String help, String placeholder) {
        this.name = name;
        this.type = type;
        this.help = help;
        this.placeholder = placeholder;
        this.values = Arrays.asList(new Object[0]);
    }

    public ParamConfig(String name, String type, Collection values) {
        this.name = name;
        this.type = type;
        this.values = values;
        this.help = "";
        this.placeholder = "";
    }

    public static ParamConfig String(String name, String help, String placeholder) {
        return new SingleParamConfig(name, "String", help, placeholder);
    }

    public static ParamConfig String(String name) {
        return String(name, "","");
    }

    public static ParamConfig Combo(String name, Collection list) {
        return new SingleParamConfig(name, "Combo", list);
    }

    public static ParamConfig ManyCombo(String name, Collection list) {
        return new MultiParamConfig(name, "ManyCombo", list);
    }

    public static ParamConfig Number(String name) {
        return new SingleParamConfig(name, "Number","","");
    }

    public static ParamConfig Boolean(String name) {
        return new BooleanParamConfig(name, "Boolean","","");
    }

    public static ParamConfig DateTime(String name) {
        return new DateTimeParamConfig(name, "DateTime","","");
    }

    public static ParamConfig Date(String name) {
        return new DateTimeParamConfig(name, "Date","","");
    }

    public static ParamConfig SQL(String name, ReportConfig reportConfig) {
        return new SqlParamConfig(name, "Combo","","", reportConfig);
    }

    public static ParamConfig Groovy(String name, String groovy) {
        return new GroovyParamConfig(name, "Groovy","","", groovy);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public String toString() {
        return "ParamConfig{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", help='" + help + '\'' +
                ", placeholder='" + placeholder + '\'' +
                '}';
    }

    public Collection<Object> getValues() {
        return values;
    }

    public void setValues(Collection<Object> values) {
        this.values = values;
    }

    public String getId() {
        Pattern replace = Pattern.compile("\\W+");
        return StringUtils.defaultIfEmpty(id, replace.matcher(getName()).replaceAll(""));
    }

    public void setId(String id) {
        this.id = id;
    }
}
