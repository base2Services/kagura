package com.base2.kagura.core.reporting.view.report.configmodel;


import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 25/07/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsConfigTest {
    @Test
    public void getReports1Test() throws URISyntaxException, MalformedURLException {
        URL reportDirectory = this.getClass().getResource("/reportTest1/");
        ReportsConfig actual = ReportsConfig.getConfig(reportDirectory.getFile());
        Assert.assertThat(actual.getErrors(), Matchers.emptyCollectionOf(String.class));
        Assert.assertThat(actual.getReports().get("TestJDBCSQL"), IsInstanceOf.instanceOf(JDBCReportConfig.class));
        Assert.assertThat(actual.getReports().get("TestJNDISQL"), IsInstanceOf.instanceOf(JNDIReportConfig.class));
        Assert.assertThat(actual.getReports().get("TestJNDISQL"), IsInstanceOf.instanceOf(JNDIReportConfig.class));
        Assert.assertThat(((JNDIReportConfig)actual.getReports().get("TestJNDISQL")).getJndi(), Is.is("java:/testDS"));
        Assert.assertThat(((JDBCReportConfig)actual.getReports().get("TestJDBCSQL")).getJdbc(), Is.is("jdbc:mysql://localhost:3306/test"));
    }
}
