package com.base2.kagura.services.camel.rest;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("")
public class FaviconRest {
    /**
     * Fav icon
     * @return favicon stream
     */
    @GET
    @Produces("image/x-icon")
    public Byte[] favicon(){return null;}


}
