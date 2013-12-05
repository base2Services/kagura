package com.base2.example.war.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: aubels
 * Date: 14/01/13
 * Time: 12:09 PM
 */
@ApplicationPath("/rest")
public class RestfulApp extends Application
{
    @Override
    public Set<Class<?>> getClasses()
    {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(AuthRestImpl.class);
        classes.add(ReportsRestImpl.class);
        classes.add(ServerRestImpl.class);
        return classes;
    }
}
