package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.GroovyReportConfig;
import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import com.base2.kagura.core.report.parameterTypes.ParamConfig;
import junit.framework.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author aubels
 *         Date: 27/05/2014
 */
public class GroovyDataReportConnectorTest {
    @Test
    public void awsImportTest()
    {
        GroovyDataReportConnector groovyDataReportConnector = new GroovyDataReportConnector(new GroovyReportConfig()
        {{
                setColumns(new ArrayList<ColumnDef>());
                setExtraOptions(new HashMap<String, String>());
                setGroovy("@GrabResolver(name = 'mvnrepository', root = 'http://repo1.maven.org/maven2')\n" +
                        "@Grapes(\n" +
                        "        @Grab(group='com.amazonaws', module='aws-java-sdk', version='1.7.9')\n" +
                        ")\n" +
                        "import com.amazonaws.auth.AWSCredentials\n" +
                        "import com.amazonaws.auth.BasicAWSCredentials\n" +
                        "\n" +
                        "AWSCredentials awsCredentials = new BasicAWSCredentials(\"\",\"\");\n" +
                        "\n" +
                        "");
                setParamConfig(new ArrayList<ParamConfig>());
                setReportId("test");
        }});
        groovyDataReportConnector.run(new HashMap<String, Object>());
    }
}
