package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.configmodel.ReportConfig;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Base representational object for a ReportConnector. A report connector is the core of a report, it takes a report
 * configuration (generally customized) and then executes the results when called.
 * User: aubels
 * Date: 22/07/13
 * Time: 11:57 AM
 */
public abstract class ReportConnector implements Serializable {
    protected Integer page = 0;
    protected Integer pageLimit = 20;
    protected List<ColumnDef> columns;
    protected List<ParamConfig> parameterConfig;
    protected List<String> errors;

    /**
     * A way to fetch the results. Some engines cache, others store.
     * @return
     */
    public abstract List<Map<String,Object>> getRows();

    /**
     * Runs the report.
     * @param extra middleware provided values, such as date/time, system configuration, logged in user, permissions
     *              and what ever else is of value to the user.
     */
    public abstract void run(Map<String, Object> extra);

    /**
     * Does a shallow copy of the necessary reportConfig values. Initializes the error structure.
     * @param reportConfig
     */
    protected ReportConnector(ReportConfig reportConfig) {
        if (reportConfig == null) return;
        this.columns = reportConfig.getColumns();
        this.parameterConfig = reportConfig.getParamConfig();
        this.errors = new ArrayList<String>();
        if (reportConfig.getPageLimit() != null)
        {
            this.pageLimit = reportConfig.getPageLimit();
        }
    }

    /**
     * Where errors are stored. Must be manually cleared.
     * @return a list of recent errors.
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     * Clears the errors from the list.
     */
    public void clearErrors()
    {
        errors = new ArrayList<String>();
    }

    /**
     * The current page to generate results for.
     * @return the current page it has been set to.
     */
    public Integer getPage() {
        return page;
    }

    /**
     * @see #getPage()
     * @param page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     * Column definitions, copied from report configuration.
     * @return columns
     */
    public List<ColumnDef> getColumns() {
        return columns;
    }

    /**
     * @see #getColumns()
     */
    public void setColumns(List<ColumnDef> columns) {
        this.columns = columns;
    }

    /**
     * Paramter configuration, copied from report configuration.
     * @return
     */
    public List<ParamConfig> getParameterConfig() {
        return parameterConfig;
    }

    /**
     * @see #getParameterConfig()
     */
    public void setParameterConfig(List<ParamConfig> parameterConfig) {
        this.parameterConfig = parameterConfig;
    }

    /**
     * The query limit, maximum number of results on a page.
     * @return the page limit
     */
    public Integer getPageLimit() {
        return pageLimit;
    }

    /**
     * @see #getPageLimit()
     */
    public void setPageLimit(Integer pageLimit) {
        this.pageLimit = pageLimit;
    }
}
