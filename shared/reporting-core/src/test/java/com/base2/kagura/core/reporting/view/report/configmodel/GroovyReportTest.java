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
public class GroovyReportTest {
    @Test
    public void getReports2Test() throws URISyntaxException, MalformedURLException {
        URL reportDirectory = this.getClass().getResource("/reportTest1/");
        ReportsConfig actual = ReportsConfig.getConfig(reportDirectory.getFile());
        Assert.assertThat(actual.getReports().get("groovy2"), IsInstanceOf.instanceOf(GroovyReportConfig.class));
        ((GroovyReportConfig)actual.getReports().get("groovy2")).getGroovy();
    }
}
