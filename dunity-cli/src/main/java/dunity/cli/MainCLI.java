package dunity.cli;

import com.api.unity.service.UnityService;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import javax.enterprise.inject.se.SeContainerInitializer;
import java.io.IOException;

public class MainCLI {

    @Parameter(names = {"-apk"}, required = true, description = "Apk path")
    private String apkPath;

    public static void main(String... argv) throws IOException {

        // setup standalone CDI
        try (var container = SeContainerInitializer.newInstance().initialize()) {
            var service = container.select(UnityService.class).get();

            var main = new MainCLI();
            JCommander.newBuilder()
                    .addObject(main)
                    .build()
                    .parse(argv);
            main.run(service);

        }
    }

    public void run(UnityService unityService) throws IOException {
        var version = unityService.version(apkPath);
        System.out.println(version);
    }

}
