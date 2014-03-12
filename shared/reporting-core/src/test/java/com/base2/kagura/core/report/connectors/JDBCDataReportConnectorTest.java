/*
   Copyright 2014 base2Services

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.freemarker.FreemarkerSQLResult;
import com.base2.kagura.core.report.parameterTypes.MultiParamConfig;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import com.base2.kagura.core.report.parameterTypes.SingleParamConfig;
import com.base2.kagura.core.report.configmodel.JDBCReportConfig;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 26/07/13
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCDataReportConnectorTest {

    public static final String JDBC = "jdbc:h2:test";

    @Test
    public void testFreemarker() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }

    @Test
    public void testFreemarkerParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table WHERE columnB=${method.value(param.test)} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput"}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerConditionalParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table WHERE <#if false>columnB=${method.value(param.test)}<#else>columnB is not null</#if> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerWhereExtensionWithWhereParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table <@where><@clause render=true>columnB=${method.value(param.test)}</@clause></@where> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput"}, actual.getParams().toArray());
    }

    @Test
    public void testFreemarkerWhereExtensionWithComplicatedWhereParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table " +
                "<@where>" +
                    "<@clause render=true>columnB=${method.value(param.test)}</@clause>" +
                    "<@clause render=false>columnC=${method.value(param.test)}</@clause>" +
                    "<@clause render=param.test='ParameterOutput'>columnD=${method.value(param.test)}</@clause>" +
                "</@where> <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"ParameterOutput","ParameterOutput"}, actual.getParams().toArray());
    }

    @Test(expected = Exception.class)
    public void testFreemarkerRequiresLimit() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test}");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
    }

    @Test
    public void testFreemarkerAllowsPageChanges() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }

    @Test
    public void testFreemarkerAllowsPageAndLimitChanges() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("Test ${param.test} <@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
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
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        Assert.assertEquals(0, actual.getParams().size());
    }

    @Test
    public void testFreemarkerWhereExtensionWithComplicatedWhereParamsWithOr() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table " +
                "<@where>" +
                    "<@where type='or'>" +
                        "<@clause render=true>columnB=${method.value(param.test)}</@clause>" +
                        "<@clause render=false>columnC=${method.value(param.test)}</@clause>" +
                    "</@where>" +
                    "<@where>" +
                        "<@clause render=false>columnE=${method.value(param.test)}</@clause>" +
                        "<@clause render=false>columnF=${method.value(param.test)}</@clause>" +
                    "</@where>" +
                    "<@where type='and'>" +
                        "<@clause render=true>columnG=${method.value(param.test)}</@clause>" +
                        "<@clause render=true>columnH=${method.value(param.test)}</@clause>" +
                    "</@where>" +
                    "<@where type='or'>" +
                        "<@clause render=true>columnJ=${method.value(param.test)}</@clause>" +
                        "<@clause render=true>columnK=${method.value(param.test)}</@clause>" +
                    "</@where>" +
                    "<@clause render=param.test='ParameterOutput'>columnD=${method.value(param.test)}</@clause>" +
                "</@where>" +
                "<@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table  WHERE columnB=? AND columnG=? AND columnH=? AND (columnJ=? OR columnK=?) AND columnD=? LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
    }
    @Test
    public void testFreemarkerWhereExtensionWithComplicatedNestedWhereParams() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table " +
                "<@where>" +
                    "<@where>" +
                        "<@clause render=true>columnB=${method.value(param.test)}</@clause>" +
                    "</@where>" +
                "</@where>" +
                "<@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                SingleParamConfig stringParam = (SingleParamConfig)ParamConfig.String("test");
                stringParam.setValue("ParameterOutput");
                add(stringParam);
            }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table  WHERE columnB=? LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[]{"ParameterOutput"}, actual.getParams().toArray());
    }
    @Test
    public void testFreemarkerValues() throws Exception {
        JDBCReportConfig reportConfig = new JDBCReportConfig();
        reportConfig.setSql("SELECT * FROM table " +
                "<@where>" +
                    "<@clause render=true>columnB IN ${method.values(param.test)}</@clause>" +
                "</@where>" +
                "<@limit />");
        reportConfig.setClassLoaderPath("org.h2.Driver");
        reportConfig.setJdbc(JDBC);
        reportConfig.setUsername("sa");
        reportConfig.setPassword("");
        reportConfig.setParamConfig(new ArrayList<ParamConfig>()
        {{
                MultiParamConfig multiParamConfig = (MultiParamConfig)ParamConfig.ManyCombo("test", Arrays.asList("1","2","3"));
                multiParamConfig.setValue(Arrays.asList("1", "2"));
                add(multiParamConfig);
        }});
        JDBCDataReportConnector jdbcDataReportConnector = new JDBCDataReportConnector(reportConfig);
        String expected = "SELECT * FROM table  WHERE columnB IN (?,?) LIMIT 20 OFFSET 0 ";
        FreemarkerSQLResult actual = jdbcDataReportConnector.freemakerParams(new HashMap<String, Object>(), true, reportConfig.getSql());
        Assert.assertEquals(expected, actual.getSql());
        org.junit.Assert.assertArrayEquals(new Object[] {"1","2"}, actual.getParams().toArray());
    }

}
