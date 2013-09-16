package com.base2.kagura.core.reporting.view.report.connectors;

import com.base2.kagura.core.reporting.view.report.FreemarkerSQLResult;
import com.base2.kagura.core.reporting.view.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.reporting.view.report.parameterTypes.SingleParamConfig;
import com.base2.kagura.core.reporting.view.report.configmodel.JDBCReportConfig;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 26/07/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCDataReportConnectorTest {
    @Test
    public void testFreemarker() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "Test ParameterOutput  LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }

    @Test
    public void testFreemarkerParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table WHERE columnB=${method.value(param.test)} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table WHERE columnB=?  LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput"}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerConditionalParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table WHERE <#if false>columnB=${method.value(param.test)}<#else>columnB is not null</#if> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table WHERE columnB is not null  LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerWhereExtensionWithWhereParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table <@where><@and render=true>columnB=${method.value(param.test)}</@and></@where> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table  WHERE columnB=?  LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput"}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerWhereExtensionWithComplicatedWhereParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table " +
                "<@where>" +
                    "<@and render=true>columnB=${method.value(param.test)}</@and>" +
                    "<@and render=false>columnC=${method.value(param.test)}</@and>" +
                    "<@and render=param.test='ParameterOutput'>columnD=${method.value(param.test)}</@and>" +
                "</@where> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table  WHERE columnB=? AND columnD=?  LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput","ParameterOutput"}, actual.getParams().toArray());
    }

    @Test(expected = Exception.class)
    public void testFreemarkerRequiresLimit() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test}");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
    }

    @Test
    public void testFreemarkerAllowsPageChanges() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        jdbcDataReportConnector.setPage(1);
        String expected = "Test ParameterOutput  LIMIT 20 OFFSET 20 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }

    @Test
    public void testFreemarkerAllowsPageAndLimitChanges() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc("jdbc:h2:");
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        jdbcDataReportConnector.setPage(2);
        jdbcDataReportConnector.setPageLimit(35);
        String expected = "Test ParameterOutput  LIMIT 35 OFFSET 70 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams();
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }


}
