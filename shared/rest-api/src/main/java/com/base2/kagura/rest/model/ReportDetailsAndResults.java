package com.base2.kagura.rest.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Map;

/**
 * @author aubels
 *         Date: 16/12/2013
 */
@XmlRootElement
public class ReportDetailsAndResults extends ReportDetails {

    private List<Map<String, Object>> rows;

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }
}
