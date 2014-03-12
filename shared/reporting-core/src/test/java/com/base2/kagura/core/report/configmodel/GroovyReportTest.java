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
package com.base2.kagura.core.report.configmodel;


import com.base2.kagura.core.storage.FileReportsProvider;
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
        ReportsConfig actual = new FileReportsProvider(reportDirectory.getFile()).getReportsConfig();
        Assert.assertThat(actual.getReports().get("groovy2"), IsInstanceOf.instanceOf(GroovyReportConfig.class));
        ((GroovyReportConfig)actual.getReports().get("groovy2")).getGroovy();
    }
}
