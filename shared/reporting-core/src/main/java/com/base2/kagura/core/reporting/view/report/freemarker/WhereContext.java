package com.base2.kagura.core.reporting.view.report.freemarker;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @author aubels
 *         Date: 18/09/13
 */
public class WhereContext {
    private WhereContext parent;
    private String connector = "AND";
    private ArrayList<String> freemarkerWhereClauses = new ArrayList<String>();

    public WhereContext(Object parent) {
        this.parent = (WhereContext)parent;
    }

    public void addWhereClause(FreemarkerWhereClause freemarkerWhereClause) {
        String outputBody = freemarkerWhereClause.getOutputBody();
        if (StringUtils.isNotBlank(outputBody))
            freemarkerWhereClauses.add(outputBody);
    }

    public void addWhereBody(String outputBody) {
        if (StringUtils.isNotBlank(outputBody))
            freemarkerWhereClauses.add(outputBody);
    }

    public WhereContext getParent() {
        return parent;
    }

    public void setParent(WhereContext parent) {
        this.parent = parent;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public ArrayList<String> getFreemarkerWhereClauses() {
        return freemarkerWhereClauses;
    }

    public void setFreemarkerWhereClauses(ArrayList<String> freemarkerWhereClauses) {
        this.freemarkerWhereClauses = freemarkerWhereClauses;
    }
}
