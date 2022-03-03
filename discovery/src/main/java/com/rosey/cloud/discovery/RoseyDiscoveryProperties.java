package com.rosey.cloud.discovery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: RoseyDiscoveryProperties <br/>
 * Description: <br/>
 * date: 2021/8/20 9:21 下午<br/>
 *
 * @author tooru<br />
 */
@ConfigurationProperties("spring.cloud.rosey.discovery")
public class RoseyDiscoveryProperties {

    @Value("${spring.cloud.rosey.namespace:${spring.profiles.active:dev}}")
    private String namespace;

    private String address;

    @Value("${spring.cloud.rosey.discovery.service:${spring.application.name:}}")
    private String service;

    private Map<String, String> metadata = new HashMap<>();

    private boolean registerEnabled = true;

    private String networkInterface = "";

    private String host;

    private int port = -1;

    private boolean secure = false;

    @Autowired
    private InetUtils inetUtils;

    @PostConstruct
    public void init() throws SocketException {
        if (StringUtils.isEmpty(host)) {
            // traversing network interfaces if didn't specify a interface
            if (StringUtils.isEmpty(networkInterface)) {
                host = inetUtils.findFirstNonLoopbackHostInfo().getIpAddress();
            }
            else {
                NetworkInterface netInterface = NetworkInterface
                        .getByName(networkInterface);
                if (null == netInterface) {
                    throw new IllegalArgumentException(
                            "no such interface " + networkInterface);
                }

                Enumeration<InetAddress> inetAddress = netInterface.getInetAddresses();
                while (inetAddress.hasMoreElements()) {
                    InetAddress currentAddress = inetAddress.nextElement();
                    if (currentAddress instanceof Inet4Address
                            && !currentAddress.isLoopbackAddress()) {
                        host = currentAddress.getHostAddress();
                        break;
                    }
                }

                if (StringUtils.isEmpty(host)) {
                    throw new RuntimeException("cannot find available ip from"
                            + " network interface " + networkInterface);
                }

            }
        }
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public boolean isRegisterEnabled() {
        return registerEnabled;
    }

    public void setRegisterEnabled(boolean registerEnabled) {
        this.registerEnabled = registerEnabled;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
