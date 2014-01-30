package com.base2.kagura.core.report.freemarker;

import com.base2.kagura.core.report.connectors.FreemarkerSQLDataReportConnector;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Simple Freemaker custom tag to insert the appropriate query (page) limit and offset (page number) for the query being run. Takes 1 optional
 * parameter called "sql" which execpts the SQL engine to be there, defaults to mysql. Outputs the appropriate style
 * LIMIT tag depending on input. Currently only supports mysql and postgres.
 * @author aubels
 *         Date: 13/09/13
 */
public class FreemarkerLimit implements TemplateDirectiveModel {
    private final Boolean[] limitExists;
    private List<String> errors;
    private FreemarkerSQLDataReportConnector freemarkerSQLDataReportConnector;

    /**
     * Constructor
     * @param limitExists A reference to an array of boolean to store if the limit has been used. In a sql statement
     *                    that is a query it is required and can only be used once. Other times it is optional
     * @param errors A reference to a list of errors, this is something to populate if there are any errors encountered
     * @param freemarkerSQLDataReportConnector A reference back to the report connector.
     */
    public FreemarkerLimit(Boolean[] limitExists, List<String> errors, FreemarkerSQLDataReportConnector freemarkerSQLDataReportConnector) {
        this.limitExists = limitExists;
        this.errors = errors;
        this.freemarkerSQLDataReportConnector = freemarkerSQLDataReportConnector;
    }

    /**
     * Inserts the appropriate LIMIT .. OFFSET .. statement into the query.
     * {@inheritDoc}
     */
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.size() > 1)
        {
            String message = "This directive doesn't allow multiple parameters.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        if (params.size() == 1 && !((Map.Entry)params.entrySet().toArray()[0]).getKey().equals("sql")) {
            String message = "This directive only takes 'sql', which you specify the type of engine, ie mysql, postgres, etc.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        Object sqlParam = params.get("sql");
        String engine = "mysql";
        if (sqlParam == null) { } else
        if (sqlParam instanceof TemplateScalarModel)
        {
            TemplateScalarModel engineParam = (TemplateScalarModel) sqlParam;
            engine = engineParam.getAsString();
        } else {
            String message = "This directive only accepts string values for 'sql'.";
            errors.add(message);
            throw new TemplateModelException(message);
        }
        if (loopVars.length != 0) throw new TemplateModelException("This directive doesn't allow loop variables.");
        limitExists[0] = true;
        int limit = freemarkerSQLDataReportConnector.getPageLimit();
        int offset = freemarkerSQLDataReportConnector.getPage() * limit;
        if (engine.equalsIgnoreCase("mysql") || engine.equalsIgnoreCase("postgres"))
        {
            env.getOut().write(" LIMIT " + limit + " OFFSET " + offset + " ");
        } else
        {
            String message = "Unknown SQL Engine " + engine + ".";
            errors.add(message);
            throw new TemplateModelException(message);
        }
    }
}