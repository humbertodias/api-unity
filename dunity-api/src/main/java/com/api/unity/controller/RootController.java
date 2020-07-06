package com.api.unity.controller;

import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Path("/")
@Singleton
public class RootController {

    @GET
    @Path("manifest")
    @Produces(MediaType.TEXT_PLAIN)
    public String manifest() throws IOException {
        var input = this.getClass().getResourceAsStream("/META-INF/MANIFEST.MF");
        return new String(input.readAllBytes(), StandardCharsets.UTF_8);
    }

}
