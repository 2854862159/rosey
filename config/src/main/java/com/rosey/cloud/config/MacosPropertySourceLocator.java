package com.rosey.cloud.config;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * ClassName: MacosPropertySourceLocator <br/>
 * Description: <br/>
 * date: 2021/7/15 10:26 下午<br/>
 *
 * @author tooru<br />
 */
public class MacosPropertySourceLocator implements PropertySourceLocator {

    private final String BASE_PATH = "/Users/tooru/rosey/myconfig";

    @Override
    public PropertySource<?> locate(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();
        CompositePropertySource composite = new CompositePropertySource("macos");
        Arrays.stream(activeProfiles).forEach(profile -> {
            MacosItem macosItem = new MacosItem();
            macosItem.setPath(BASE_PATH + "/" + profile + "/macos.properties");
            macosItem.setFile(new File(BASE_PATH + "/macos.properties"));
            macosItem.setDirPath(Paths.get(BASE_PATH + "/" + profile));
            try {
                MacosPropertySource macosPropertySource = new MacosPropertySource(BASE_PATH + "#" + profile, macosItem);
                composite.addPropertySource(macosPropertySource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return composite;
    }
}
