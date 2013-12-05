package com.base2.example.war.rest;

import com.base2.kagura.rest.ServerRest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author aubels
 *         Date: 26/08/13
 */
@Path("/server")
@RequestScoped
public class ServerRestImpl extends ServerRest {
    @Override
    public String test()
    {
        return "Test";
    }
}
