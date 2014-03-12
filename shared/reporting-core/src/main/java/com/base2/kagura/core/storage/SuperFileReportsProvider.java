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
package com.base2.kagura.core.storage;

import com.base2.kagura.core.report.configmodel.ReportsConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Just like hte FileReportsProvider, however in this one, instead of having each report in it's own directory, it's in
 * one giant Map in a single Yaml file. In some cases this should be more preferable. However this might have weaker
 * security as it loads all reports. One case would be on a file system that does not support directory listing.
 * @author aubels
 *         Date: 15/10/13
 */
public class SuperFileReportsProvider extends ReportsProvider<File> {
    String superReportFile;

    /**
     * Constructor, point the URI of the mega-file.
     * @param superReportFile
     */
    public SuperFileReportsProvider(String superReportFile) {
        this.superReportFile = superReportFile;
    }

    /**
     * Returns the super file location.
     * @return
     */
    public String getSuperReportFile() {
        return superReportFile;
    }

    /**
     * @see #getSuperReportFile()
     * @param superReportFile
     */
    public void setSuperReportFile(String superReportFile) {
        this.superReportFile = superReportFile;
    }

    /** {@inheritDoc} */
    @Override
    protected String loadReport(ReportsConfig result, File report) throws Exception {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected File[] getReportList() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public ReportsConfig getReportsConfig(Collection<String> restrictToNamed) {
        return getReportsConfig();
    }

    /** {@inheritDoc} */
    @Override
    public ReportsConfig getReportsConfig() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ReportsConfig reportsConfig = null;
        try {
            reportsConfig = mapper.readValue(new File(superReportFile), ReportsConfig.class);
        } catch (IOException e) {
            e.printStackTrace();
            errors.add("Error parsing super report " + superReportFile);
        }
        return reportsConfig;

    }

    /** {@inheritDoc} -- Needs to be fixed. */
    @Override
    protected String reportToName(File report) {
        return null;
    }
}
