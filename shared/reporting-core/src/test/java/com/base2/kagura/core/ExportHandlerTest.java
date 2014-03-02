package com.base2.kagura.core;

import com.base2.kagura.core.report.configmodel.parts.ColumnDef;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author aubels
 *         Date: 3/03/2014
 */
public class ExportHandlerTest {
    @Test
    public void noColumnsCSVTest()
    {
        ExportHandler exportHandler = new ExportHandler();
        exportHandler.generateCsv(new ByteArrayOutputStream(), new ArrayList<Map<String, Object>>(), new ArrayList<ColumnDef>());
    }
}
