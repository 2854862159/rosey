package com.rosey.cloud.config;

import org.springframework.core.env.PropertySource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ClassName: MacosPropertySource <br/>
 * Description: <br/>
 * date: 2021/7/15 10:33 下午<br/>
 *
 * @author tooru<br />
 */
public class MacosPropertySource extends PropertySource<MacosItem> {

    private Map<String, String> properties = new LinkedHashMap();

    public MacosPropertySource(String name, MacosItem macosItem) throws IOException {
        super(name, macosItem);
        Path filePath = Paths.get(macosItem.getPath());
        read(filePath);

        new Thread(() -> {
            try{
                WatchService watchService
                        = FileSystems.getDefault().newWatchService();

                macosItem.getDirPath().register(
                        watchService,
                        StandardWatchEventKinds.ENTRY_MODIFY);

                WatchKey key;
                while ((key = watchService.take()) != null) {
                    for (WatchEvent<?> event : key.pollEvents()) {
                        read(filePath);
                    }
                    key.reset();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public Object getProperty(String s) {
        return properties.get(s);
    }

    private void read(Path path) throws IOException {
        Files.lines(path, StandardCharsets.UTF_8).forEach(line -> {
            String[] split = line.split("=");
            properties.put(split[0], split[1]);
        });
    }
}
