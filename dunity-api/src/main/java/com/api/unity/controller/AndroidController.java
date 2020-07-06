package com.api.unity.controller;

import com.api.unity.helper.FileStreamingOutput;
import com.api.unity.service.AndroidService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/android")
@Singleton
public class AndroidController {

    @Inject
    @ConfigProperty(name = "apk.downloader.url")
    String apkDownloaderUrl;

    @Inject
    AndroidService androidService;

    @PostConstruct
    void setup() {
        androidService.postConstruct(apkDownloaderUrl);
    }

    @GET
    @Path("/")
    public String[] files() {
        return androidService.files();
    }

    @GET
    @Path("/url/{id}")
    public String url(@PathParam("id") String id) throws IOException {
        return androidService.getUrlApkDownload(id);
    }

    @GET
    @Path("/download/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response download(@PathParam("id") String id) throws IOException {
        var file = androidService.getFile(id);
        var stream = new FileStreamingOutput(file);
        return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("content-length", file.length())
                .header("content-disposition", String.format("attachment; filename=%s",file.getName())).build();
    }

    @GET
    @Path("/manifestXml/{id}")
    @Produces(MediaType.TEXT_XML)
    public String manifestXml(@PathParam("id") String id) throws IOException {
        return androidService.manifestXml(id);
    }

    @GET
    @Path("/manifestMf/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public byte[] manifestMf(@PathParam("id") String id) throws IOException {
        return androidService.manifestMf(id);
    }

    @GET
    @Path("/unity/version/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response unityVersion(@PathParam("id") String id) throws IOException {
        return Response.ok(androidService.unityVersion(id)).build();
    }

    @GET
    @Path("/meta/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String meta(@PathParam("id") String id) throws IOException {
        return androidService.meta(id);
    }

}
