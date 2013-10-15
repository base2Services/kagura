package com.base2.kagura.services.camel.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author aubels
 *         Date: 15/10/13
 */
@Service
public class FileReportsProvider extends com.base2.kagura.core.storage.FileReportsProvider {
    private static final Logger LOG = LoggerFactory.getLogger(FileReportsProvider.class);

    public FileReportsProvider() {
        super(null);
        LOG.info("Created File Reports Provider");
    }

    @Override
    public String getReportDirectory() {
        return super.getReportDirectory();
    }

    @Value("${com.base2.kagura.reportloc:/TestReports/}")
    @Override
    public void setReportDirectory(String reportDirectory) {
        super.setReportDirectory(reportDirectory);
        LOG.info("Loaded reports directory: {}", reportDirectory);
    }
}
