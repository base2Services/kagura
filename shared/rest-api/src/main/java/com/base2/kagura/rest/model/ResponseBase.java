package com.base2.kagura.rest.model;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 13/12/2013
 */
public class ResponseBase {
    String error;
    String reportId;
    List<String> errors;
    Map<String, String> extra;

    public ResponseBase() {
    }

    public ResponseBase(Map<String, Object> result) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        BeanUtils.populate(this, result);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }
}
