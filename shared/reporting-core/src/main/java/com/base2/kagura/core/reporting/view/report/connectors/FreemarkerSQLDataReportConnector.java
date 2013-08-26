package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.*;
import com.base2.kagura.core.reporting.view.report.configmodel.FreeMarkerSQLReportConfig;
import com.base2.kagura.core.reporting.view.report.freemarker.FreemarkerWhere;
import com.base2.kagura.core.reporting.view.report.freemarker.FreemarkerWhereClause;
import freemarker.core.Environment;
import freemarker.template.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FreemarkerSQLDataReportConnector extends ReportConnector {
    private final String freemarkerSql;
    private List<Map<String, Object>> rows;

    public FreemarkerSQLDataReportConnector(FreeMarkerSQLReportConfig reportConfig) {
        super(reportConfig);
        this.freemarkerSql = reportConfig.getSql();
    }

    protected Connection connection;

    @Override
    public void run() {
        PreparedStatement statement = null;
        if (connection == null)
        {
            return;
        }
        try {
            FreemarkerSQLResult freemarkerSQLResult = freemakerParams();
            String sql = freemarkerSQLResult.getSql();
            statement = connection.prepareStatement(sql);
            for(int i=0;i<freemarkerSQLResult.getParams().size();i++) {
                statement.setString(i+1, freemarkerSQLResult.getParams().get(i));
            }
            rows = resultSetToMap(statement.executeQuery());
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                    statement = null;
                }
            } catch (SQLException e) {
                errors.add(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected FreemarkerSQLResult freemakerParams() throws Exception {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Create the root hash
        Map root = new HashMap();
        Map params = new HashMap();
        root.put("param", params);
//        root.put("test", "");
        if (parameterConfig != null)
        {
            for (ParamConfig paramConfig : parameterConfig)
            {
                if (paramConfig instanceof SingleParamConfig)
                    params.put(paramConfig.getId(), ((SingleParamConfig)paramConfig).getValue());
                if (paramConfig instanceof MultiParamConfig)
                    params.put(paramConfig.getId(), ((MultiParamConfig)paramConfig).getValue());
                if (paramConfig instanceof BooleanParamConfig)
                    params.put(paramConfig.getId(), ((BooleanParamConfig)paramConfig).getValue());
            }
        }
        Map methods = new HashMap();
        root.put("method", methods);
        final List<String> usedParams = new ArrayList<String>();
        methods.put("value", new TemplateMethodModel() {
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                if (arguments.get(0) == null)
                    usedParams.add(null);
                else
                    usedParams.add(arguments.get(0).toString());
                return "?";
            }
        });
        final Boolean[] limitExists = {false};
        root.put("where", new FreemarkerWhere(errors));
        root.put("and", new FreemarkerWhereClause(errors));
        root.put("limit", new TemplateDirectiveModel() {
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
                int limit = getPageLimit();
                int offset = getPage() * limit;
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
        });

        Template temp = null;
        StringWriter out = new StringWriter();
        try {
            temp = new Template(null, new StringReader(freemarkerSql), cfg);
            temp.process(root, out);
        } catch (TemplateException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
        if (!limitExists[0]) throw new Exception("Could not find <@limit sql=mysql /> or <@limit sql=postgres /> tag on query.");
        return new FreemarkerSQLResult(out.toString(), usedParams);
    }

    public List<Map<String,Object>> resultSetToMap(ResultSet rows) {
        try {
            List<Map<String,Object>> beans = new ArrayList<Map<String,Object>>();
            int columnCount = rows.getMetaData().getColumnCount();
            while (rows.next()) {
                Map<String,Object> bean = new HashMap<String, Object>();
                beans.add(bean);
                for (int i = 0; i < columnCount; i++) {
                    Object object = rows.getObject(i + 1);
                    String columnLabel = rows.getMetaData().getColumnLabel(i + 1);
                    String columnName = rows.getMetaData().getColumnName(i + 1);
                    bean.put(StringUtils.defaultIfEmpty(columnLabel, columnName), object != null ? object.toString() : "");
                }
            }
            return beans;
        } catch (Exception ex) {
            errors.add(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}
