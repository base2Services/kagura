package com.base2.kagura.shared;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author aubels
 *         Date: 27/08/13
 */
public class ResourcesUtils {
    public static InputStream getFaviconPng()
    {
        return ResourcesUtils.class.getResourceAsStream("/favicon.png");
    }
}
