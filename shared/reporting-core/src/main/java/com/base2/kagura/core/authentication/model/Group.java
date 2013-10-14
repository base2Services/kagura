package com.base2.kagura.core.authentication.model;

import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class Group {
    private String groupname;
    private List<String> reports;
    private Map<String, Object> extraOptions;

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public List<String> getReports() {
        return reports;
    }

    public void setReports(List<String> reports) {
        this.reports = reports;
    }

    public Map<String, Object> getExtraOptions() {
        return extraOptions;
    }

    public void setExtraOptions(Map<String, Object> extraOptions) {
        this.extraOptions = extraOptions;
    }
}
