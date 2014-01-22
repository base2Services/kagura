package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.FreeMarkerSQLReportConfig;
import com.base2.kagura.core.report.freemarker.FreemarkerLimit;
import com.base2.kagura.core.report.freemarker.FreemarkerSQLResult;
import com.base2.kagura.core.report.freemarker.FreemarkerWhere;
import com.base2.kagura.core.report.freemarker.FreemarkerWhereClause;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import freemarker.template.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FreemarkerSQLDataReportConnector extends ReportConnector {
    private final String freemarkerSql;
    private final String presql;
    private final String postsql;
    private List<Map<String, Object>> rows;
    protected int queryTimeout;

    public FreemarkerSQLDataReportConnector(FreeMarkerSQLReportConfig reportConfig) {
        super(reportConfig);
        this.freemarkerSql = reportConfig.getSql();
        this.postsql = reportConfig.getPostsqlsql();
        this.presql = reportConfig.getPresqlsql();
        this.queryTimeout = reportConfig.getQueryTimeout();
    }

    protected Connection connection;

    @Override
    public void run(Map<String, Object> extra) {
        PreparedStatement statement = null;
        try {
            getStartConnection();
            if (StringUtils.isNotBlank(presql))
            {
                FreemarkerSQLResult prefreemarkerSQLResult = freemakerParams(extra, false, presql);
                statement = connection.prepareStatement(prefreemarkerSQLResult.getSql());
                for(int i=0;i<prefreemarkerSQLResult.getParams().size();i++) {
                    statement.setObject(i + 1, prefreemarkerSQLResult.getParams().get(i));
                }
                statement.setQueryTimeout(queryTimeout);
                statement.executeBatch();
            }
            FreemarkerSQLResult freemarkerSQLResult = freemakerParams(extra, true, freemarkerSql);
            statement = connection.prepareStatement(freemarkerSQLResult.getSql());
            for(int i=0;i<freemarkerSQLResult.getParams().size();i++) {
                statement.setObject(i + 1, freemarkerSQLResult.getParams().get(i));
            }
            statement.setQueryTimeout(queryTimeout);
            rows = resultSetToMap(statement.executeQuery());
            if (StringUtils.isNotBlank(postsql))
            {
                FreemarkerSQLResult postfreemarkerSQLResult = freemakerParams(extra, false, postsql);
                statement = connection.prepareStatement(postfreemarkerSQLResult.getSql());
                for(int i=0;i<postfreemarkerSQLResult.getParams().size();i++) {
                    statement.setObject(i + 1, postfreemarkerSQLResult.getParams().get(i));
                }
                statement.setQueryTimeout(queryTimeout);
                statement.executeBatch();
            }
        } catch (Exception ex) {
            errors.add(ex.getMessage());
        } finally {
            try {
                if (statement != null && !statement.isClosed())
                {
                    statement.close();
                    statement = null;
                }
                if (connection != null && !connection.isClosed())
                {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                errors.add(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected FreemarkerSQLResult freemakerParams(Map<String, Object> extra, boolean requireLimit, String sql) throws Exception {
        Configuration cfg = new Configuration();
        cfg.setDateFormat("yyyy-MM-dd");
        cfg.setDateTimeFormat("yyyy-MM-dd hh:mm:ss");
        cfg.setTimeFormat("hh:mm:ss");
        cfg.setObjectWrapper(new DefaultObjectWrapper());
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Create the root hash
        Map root = new HashMap();
        Map params = new HashMap();
        root.put("extra", extra);
        root.put("param", params);
        if (parameterConfig != null)
        {
            for (ParamConfig paramConfig : parameterConfig)
            {
                params.put(paramConfig.getId(), PropertyUtils.getProperty(paramConfig, "value"));
            }
        }
        Map methods = new HashMap();
        root.put("method", methods);
        final List<Object> usedParams = new ArrayList<Object>();
        methods.put("value", new TemplateMethodModel() {
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                usedParams.add(arguments.get(0));
                return "?";
            }
        });
        methods.put("values", new TemplateMethodModelEx() {
            @Override
            public Object exec(List arguments) throws TemplateModelException {
                final Object param1 = arguments.get(0);
                List<String> result = new ArrayList<String>();
                if (param1 instanceof SimpleSequence)
                {
                    for (Object object : ((SimpleSequence)param1).toList())
                    {
                        usedParams.add(object);
                        result.add("?");
                    }
                } else if (param1 instanceof SimpleCollection)
                {
                    final TemplateModelIterator iterator = ((SimpleCollection) param1).iterator();
                    while (iterator.hasNext())
                    {
                        usedParams.add(iterator.next());
                        result.add("?");
                    }
                } else {
                    usedParams.add(arguments.get(0));
                    result.add("?");
                }
                return "(" + StringUtils.join(result, ",") + ")";
            }
        });
        final Boolean[] limitExists = {false};
        root.put("where", new FreemarkerWhere(errors));
        root.put("clause", new FreemarkerWhereClause(errors));
        root.put("limit", new FreemarkerLimit(limitExists, errors, this));

        Template temp = null;
        StringWriter out = new StringWriter();
        try {
            temp = new Template(null, new StringReader(sql), cfg);
            temp.process(root, out);
        } catch (TemplateException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
        if (requireLimit && !limitExists[0]) throw new Exception("Could not find <@limit sql=mysql /> or <@limit sql=postgres /> tag on query.");
        return new FreemarkerSQLResult(out.toString(), usedParams);
    }

    public List<Map<String,Object>> resultSetToMap(ResultSet rows) {
        try {
            List<Map<String,Object>> beans = new ArrayList<Map<String,Object>>();
            int columnCount = rows.getMetaData().getColumnCount();
            while (rows.next()) {
                LinkedHashMap<String,Object> bean = new LinkedHashMap<String, Object>();
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

    protected abstract void getStartConnection() throws NamingException, SQLException;
}
