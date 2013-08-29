package com.base2.kagura.services.camel.model;

import java.util.List;

/**
 * @author aubels
 *         Date: 29/08/13
 */
public class Group {
    private String groupname;
    private List<String> reports;

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
}
