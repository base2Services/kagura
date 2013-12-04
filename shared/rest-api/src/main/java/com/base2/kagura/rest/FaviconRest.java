package com.base2.kagura.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * @author aubels
 *         Date: 26/08/13
 */
public class FaviconRest {
    /**
     * Fav icon
     * @return favicon stream
     */
    @GET
    @Produces("image/x-icon")
    public Byte[] favicon(){return null;}
}
