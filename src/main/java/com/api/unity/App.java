package com.api.unity;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.servers.Server;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(info = @Info(
        title = "Api-Unity",
        version = "1.0.0"),
        servers = {
                @Server(url = "api-unity.herokuapp.com",description = "Heroku"),
                @Server(url = "localhost:8080",description = "8080"),
        }
)
@ApplicationPath("/api")
@ApplicationScoped
public class App extends Application {
}
