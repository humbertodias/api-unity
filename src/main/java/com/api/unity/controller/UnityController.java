package com.api.unity.controller;

import com.api.unity.service.AndroidService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/unity")
@Singleton
public class UnityController {

    @Inject
    AndroidService androidService;

    @GET
    @Path("/version/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String unityVersion(@PathParam("id") String id) throws IOException {
        return androidService.unityVersion(id);
    }

    @GET
    @Path("/mono/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean unityMono(@PathParam("id") String id) throws IOException {
        return androidService.unityMono(id);
    }

    @GET
    @Path("/il2cpp/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean unityIl2cpp(@PathParam("id") String id) throws IOException {
        return androidService.unityIlcpp(id);
    }



}
