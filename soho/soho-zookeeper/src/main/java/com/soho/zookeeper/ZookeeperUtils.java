package com.soho.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * Created by Administrator on 2018/3/29.
 */
public class ZookeeperUtils {

    static final String CONNECT_ADDR = "192.168.1.104:2181,192.168.1.105:2181,192.168.1.107:2181";
    static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(new ZkConnection(CONNECT_ADDR), SESSION_TIMEOUT);
    }

}
