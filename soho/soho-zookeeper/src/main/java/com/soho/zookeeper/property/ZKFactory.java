package com.soho.zookeeper.property;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取ZooKeeper中心配置信息
 *
 * @author shadow
 */
public class ZKFactory {

    /**
     * 创建链接客户端
     *
     * @param config
     * @return CuratorFramework
     */
    private static CuratorFramework buildZkClient(ZKConfig config) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(config.getSleepTimeMs(), config.getMaxRetries());
        byte aclAuth[] = null;
        if (!StringUtils.isEmpty(config.getAclAuth())) {
            aclAuth = config.getAclAuth().trim().getBytes();
        } else {
            aclAuth = "".getBytes();
        }
        return CuratorFrameworkFactory.builder().connectString(config.getConnectionUrl()).retryPolicy(retryPolicy)
                .connectionTimeoutMs(config.getConnectionTimeoutMs()).authorization("digest", aclAuth)
                .sessionTimeoutMs(config.getSessionTimeoutMs()).build();
    }

    /**
     * 读取配置中心信息到MAP集合
     *
     * @param config
     * @return Map<String, Object>
     */
    public static Map<String, Object> loadProperties(ZKConfig config) {
        Map<String, Object> zkProperties = new HashMap<>();
        if (!StringUtils.isEmpty(config.getEnable())) {
            boolean enable = Boolean.valueOf(config.getEnable().trim());
            if (!enable) {
                return zkProperties;
            }
        }
        CuratorFramework client = buildZkClient(config);
        client.start();
        String versionedRootNode = ZKPaths.makePath(config.getRootNode().trim(), config.getVersion().trim());
        try {
            String[] groups = config.getGroupNodes().split(",");
            if (groups != null) {
                for (String group : groups) {
                    String versionedGroupNode = ZKPaths.makePath(versionedRootNode, group.trim());
                    List<String> nodeKeys = client.getChildren().forPath(versionedGroupNode);
                    for (String nodeKey : nodeKeys) {
                        byte[] be = client.getData().forPath(ZKPaths.makePath(versionedGroupNode, nodeKey.trim()));
                        if (be != null) {
                            zkProperties.put(nodeKey.trim(), new String(be, "utf-8").trim());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return zkProperties;
    }

}
