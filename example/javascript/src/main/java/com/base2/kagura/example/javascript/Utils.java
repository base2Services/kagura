package com.base2.kagura.example.javascript;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author aubels
 *         Date: 3/09/13
 */
public class Utils {
    public static String serverPath(HttpServletRequest request)
    {
        try {
            String contextPath = request.getContextPath() != null ? request.getContextPath() : "";
            if (!contextPath.endsWith("/") && contextPath.length() > 0)
                contextPath += "/";
            return new URL("http", request.getServerName(), request.getServerPort(),(!contextPath.startsWith("/")? "/":"") + contextPath).toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
