package com.base2.kagura.services.camel.kagura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author aubels
 *         Date: 28/08/13
 */
@Service
public class ReportBean {
    private ServerBean serverBean;



    @Autowired
    public void setServerBean(ServerBean serverBean) {
        this.serverBean = serverBean;
    }

    public ServerBean getServerBean() {
        return serverBean;
    }
}
