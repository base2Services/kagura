package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.JDBCReportConfig;
import org.apache.commons.lang3.StringUtils;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class JDBCDataReportConnector extends FreemarkerSQLDataReportConnector {

    public JDBCDataReportConnector(JDBCReportConfig reportConfig) {
        super(reportConfig);
        Properties connectionProps = new Properties();
        if (StringUtils.isNotBlank(reportConfig.getUsername()))
            connectionProps.put("user", reportConfig.getUsername());
        if (StringUtils.isNotBlank(reportConfig.getPassword()))
            connectionProps.put("password", reportConfig.getPassword());
        try {
            if (StringUtils.isNotBlank(reportConfig.getClassLoaderPath()))
                try
                {
                    Class.forName(reportConfig.getClassLoaderPath());
                } catch (ClassNotFoundException e) {
                    errors.add(e.getMessage());
                    e.printStackTrace();
                }
            connection = DriverManager.getConnection(reportConfig.getJdbc(), connectionProps);
        } catch (SQLException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
    }
}
