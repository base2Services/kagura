package com.base2.kagura.core.report.configmodel;


import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.Matchers.*;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 25/07/13
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GroovyParamConfigTest {
    @Test
    public void getReports1Test() throws URISyntaxException, MalformedURLException {
        URL reportDirectory = this.getClass().getResource("/reportTest1/");
        ReportsConfig actual = ReportsConfig.getConfig(reportDirectory.getFile());
        Assert.assertThat(actual.getErrors(), emptyCollectionOf(String.class));
        Assert.assertThat(actual.getReports().get("groovy1"), IsInstanceOf.instanceOf(FakeReportConfig.class));
        Assert.assertThat(actual.getReports().get("groovy1").getParamConfig().get(0).getValues(), contains((Object)"1","2","3","4","5"));
        Assert.assertThat(actual.getErrors(), empty());
    }
}
