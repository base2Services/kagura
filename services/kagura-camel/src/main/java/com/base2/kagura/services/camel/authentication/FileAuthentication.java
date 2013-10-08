package com.base2.kagura.services.camel.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author aubels
 *         Date: 7/10/13
 */
@Service()
public class FileAuthentication extends com.base2.kagura.core.reporting.view.authentication.FileAuthentication {
    private static final Logger LOG = LoggerFactory.getLogger(FileAuthentication.class);

    public FileAuthentication() {
        super();
    }

    @Override
    public String getConfigPath() {
        return super.getConfigPath();
    }

    @Override
    @Value("${com.base2.kagura.reportloc:/TestReports/}")
    public void setConfigPath(String configPath) {
        super.setConfigPath(configPath);
        LOG.info("Got URL {}", configPath);
    }
}
