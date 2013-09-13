package com.base2.kagura.core.reporting.view.report.freemarker;

import com.base2.kagura.core.reporting.view.report.connectors.FreemarkerSQLDataReportConnector;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 13/09/13
 */
public class FreemarkerLimit implements TemplateDirectiveModel {
    private final Boolean[] limitExists;
    private List<String> errors;
    private FreemarkerSQLDataReportConnector freemarkerSQLDataReportConnector;

    public FreemarkerLimit(Boolean[] limitExists, List<String> errors, FreemarkerSQLDataReportConnector freemarkerSQLDataReportConnector) {
        this.limitExists = limitExists;
        this.errors = errors;
        this.freemarkerSQLDataReportConnector = freemarkerSQLDataReportConnector;
    }

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