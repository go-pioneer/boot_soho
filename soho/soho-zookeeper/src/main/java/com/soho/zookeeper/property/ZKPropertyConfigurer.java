package com.soho.zookeeper.property;

import com.soho.spring.extend.AbstractPropertyConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.util.Properties;

/**
 * Spring加载配置文件,以及ZooKeeper中心配置信息
 *
 * @author shadow
 */
public class ZKPropertyConfigurer extends AbstractPropertyConfigurer {

    private final static String CONNECTION_URL = "zookeeper.connectionUrl";
    private final static String ACL_AUTH = "zookeeper.aclAuth";
    private final static String VERSION = "zookeeper.version";
    private final static String ROOT_NODE = "zookeeper.rootNode";
    private final static String GROUP_NODES = "zookeeper.groupNodes";
    private final static String ENABLE = "zookeeper.enable";
    private final static String TIME_OUT = "zookeeper.timeout";

    public ZKPropertyConfigurer(String filePath, String[] decodeKeys) {
        super(filePath, decodeKeys);
    }

    @Override
    public Properties appendProperties(Properties properties) {
        String enable = properties.getProperty(ENABLE);
        String connectionUrl = properties.getProperty(CONNECTION_URL);
        String aclAuth = properties.getProperty(ACL_AUTH);
        String connectionTimeoutMs = properties.getProperty(TIME_OUT);
        String version = properties.getProperty(VERSION);
        String rootNode = properties.getProperty(ROOT_NODE);
        String groupNodes = properties.getProperty(GROUP_NODES);
        ZKConfig config = new ZKConfig();
        config.setEnable(enable);
        config.setConnectionUrl(connectionUrl);
        config.setAclAuth(aclAuth);
        config.setConnectionTimeoutMs(connectionTimeoutMs != null ? Integer.parseInt(connectionTimeoutMs) : 100000);
        config.setVersion(version);
        config.setRootNode(rootNode);
        config.setGroupNodes(groupNodes);
        properties.putAll(ZKFactory.loadProperties(config));
        return properties;
    }

}
