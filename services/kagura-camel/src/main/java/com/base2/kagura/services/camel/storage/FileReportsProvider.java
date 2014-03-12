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
