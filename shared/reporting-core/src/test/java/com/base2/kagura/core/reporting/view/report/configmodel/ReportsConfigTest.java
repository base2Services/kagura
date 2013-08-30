package com.base2.kagura.core.reporting.view.report.configmodel;


import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import java.net.URISyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 25/07/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReportsConfigTest {
    @Test
    public void getReports1Test() throws URISyntaxException {
        String reportDirectory = this.getClass().getResource("/reportTest1").getFile();
        ReportsConfig actual = ReportsConfig.getConfig(reportDirectory);
        Assert.assertThat(actual.getReports().get("TestJDBCSQL"), IsInstanceOf.instanceOf(JDBCReportConfig.class));
        Assert.assertThat(actual.getReports().get("TestJNDISQL"), IsInstanceOf.instanceOf(JNDIReportConfig.class));
        Assert.assertThat(actual.getReports().get("TestJNDISQL"), IsInstanceOf.instanceOf(JNDIReportConfig.class));
        Assert.assertThat(((JNDIReportConfig)actual.getReports().get("TestJNDISQL")).getJndi(), Is.is("java:/testDS"));
        Assert.assertThat(((JDBCReportConfig)actual.getReports().get("TestJDBCSQL")).getJdbc(), Is.is("jdbc:mysql://localhost:3306/test"));
    }
}
