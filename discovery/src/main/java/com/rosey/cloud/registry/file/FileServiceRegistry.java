package com.rosey.cloud.registry.file;

import com.rosey.cloud.discovery.RoseyServiceInstance;
import com.rosey.cloud.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName: FileServiceRegistry <br/>
 * Description: <br/>
 * date: 2021/8/21 1:49 下午<br/>
 *
 * @author tooru<br />
 */
public class FileServiceRegistry implements Registry, EnvironmentAware {

    private static final Logger log = LoggerFactory.getLogger(FileServiceRegistry.class);

    private final static String ROOT_PATH = "/Users/tooru/rosey/registry_info";

    private final static ConcurrentHashMap<String, Map<String, String>> metaContainer = new ConcurrentHashMap<>();

    private Environment environment;

    @Override
    public void registerInstance(String namespace, String service, String ip, Integer port) {
        registerInstance(namespace, service, ip, port, new HashMap<>());
    }

    @Override
    public void registerInstance(String namespace, String service, String ip, Integer port, Map<String, String> metadata) {
        Path registerPath = Paths.get(ROOT_PATH, namespace, service);

        try{
            registerPath = Files.createDirectories(registerPath);
            Path filePath = Files.createFile(Paths.get(registerPath.toString(), String.join("_", service, ip, String.valueOf(port))));
            try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)){
                for (Map.Entry<String, String> entry: metadata.entrySet()) {
                    writer.write(String.join("=", entry.getKey(), entry.getValue()) + "\n");
                }

                writer.write("status=UP\n");
                writer.flush();
            }

            metaContainer.put(service, metadata);
        }catch (IOException ex){
            log.error("注册失败:{}", ex.getMessage());
        }
    }

    @Override
    public void deregisterInstance(String namespace, String service, String ip, Integer port) {
        Path deregisterPath = Paths.get(ROOT_PATH, namespace, service, String.join("_", service, ip, String.valueOf(port)));

        try{
            Files.delete(deregisterPath);
        }catch (IOException ex){
            log.error("注销失败:{}", ex.getMessage());
        }
    }

    @Override
    public void updateInstance(String namespace, String service, String ip, Integer port, Map<String, String> metadata) {
        Path filePath = Paths.get(ROOT_PATH, namespace, service, String.join("_", service, ip, String.valueOf(port)));
        try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)){
            writer.write("");
            writer.flush();

            for (Map.Entry<String, String> entry: metadata.entrySet()) {
                writer.write(String.join("=", entry.getKey(), entry.getValue()));
            }

            writer.flush();
            metaContainer.put(service, metadata);
        }catch (IOException ex){
            log.error("修改元数据异常,{}", ex);
        }
    }

    @Override
    public Object getStatus(String service) {
        return metaContainer.get(service).get("status");
    }

    @Override
    public List<ServiceInstance> getServiceInstance(String service) {
        return getServiceInstance(environment.getActiveProfiles()[0], service);
    }

    @Override
    public List<ServiceInstance> getServiceInstance(String namespace, String service) {
        List<ServiceInstance> serviceInstances = new ArrayList<>();
        Path registerPath = Paths.get(ROOT_PATH, namespace, service);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(registerPath)){
            for(Path path : stream){
                RoseyServiceInstance serviceInstance = new RoseyServiceInstance();
                serviceInstance.setServiceId(service);
                Path fileName = path.getFileName();
                String[] s = fileName.toString().split("_");
                serviceInstance.setPort(Integer.valueOf(s[s.length - 1]));
                serviceInstance.setHost(s[s.length - 2]);

                Map<String, String> metadata = new HashMap<>();
                try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
                    String str = null;
                    while((str = reader.readLine()) != null){
                        String[] split = str.split("=");
                        metadata.put(split[0], split[1]);
                    }
                }

                serviceInstance.setMetadata(metadata);
                serviceInstances.add(serviceInstance);
            }
        }catch(IOException e){
            log.info("读取异常", e.getMessage());
        }

        return serviceInstances;
    }

    @Override
    public List<String> getService() {
        return getService(environment.getActiveProfiles()[0]);
    }

    @Override
    public List<String> getService(String namespace) {
        List<String> services = new ArrayList<>();
        Path registerPath = Paths.get(ROOT_PATH, namespace);
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(registerPath)){
            for(Path path : stream){
                services.add(path.getFileName().toString());
            }
        }catch(IOException e){
            log.info("读取异常", e.getMessage());
        }
        return null;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
