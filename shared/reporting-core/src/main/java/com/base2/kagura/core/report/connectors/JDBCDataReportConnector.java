package com.base2.kagura.core.report.connectors;

import com.base2.kagura.core.report.configmodel.JDBCReportConfig;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * JDBC database backend, used by FreemarkerSQLDataReportConnector.
 * User: aubels
 * Date: 24/07/13
 * Time: 4:39 PM
 */
public class JDBCDataReportConnector extends FreemarkerSQLDataReportConnector {
    private String jdbc;
    private Properties connectionProps;

    /**
     * Constructor. Does a shallow copy of needed values, also prepares parameters and tests the connection. Does the
     * ClassPath search too.
     * @param reportConfig
     */
    public JDBCDataReportConnector(JDBCReportConfig reportConfig) {
        super(reportConfig);
        try {
            if (StringUtils.isNotBlank(reportConfig.getClassLoaderPath()))
                try
                {
                    Class.forName(reportConfig.getClassLoaderPath());
                } catch (ClassNotFoundException e) {
                    errors.add(e.getMessage());
                    e.printStackTrace();
                }
            connectionProps = new Properties();
            if (StringUtils.isNotBlank(reportConfig.getUsername()))
                connectionProps.put("user", reportConfig.getUsername());
            if (StringUtils.isNotBlank(reportConfig.getPassword()))
                connectionProps.put("password", reportConfig.getPassword());
            setJdbc(reportConfig.getJdbc());
            getStartConnection();
            connection.close();
            connection = null;
        } catch (SQLException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        } catch (NamingException e) {
            errors.add(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void getStartConnection() throws NamingException, SQLException {
        connection = DriverManager.getConnection(getJdbc(), connectionProps);
    }

    /**
     * The JDBC connection URL
     * @return
     */
    public String getJdbc() {
        return jdbc;
    }

    /**
     * @see #getJdbc()
     */
    public void setJdbc(String jdbc) {
        this.jdbc = jdbc;
    }
}
