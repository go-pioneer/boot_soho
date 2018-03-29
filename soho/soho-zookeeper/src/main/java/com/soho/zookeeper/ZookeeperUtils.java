package com.soho.zookeeper;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/3/29.
 */
public class ZookeeperUtils {

    static final String CONNECT_ADDR = "39.108.64.191:31181";
    static final int SESSION_TIMEOUT = 5000;

    public static void main(String[] args) {
        ZkClient zkClient = new ZkClient(new ZkConnection(CONNECT_ADDR), SESSION_TIMEOUT);
//        System.out.println(zkClient.exists("/chaseyo/dev1.0.0/dbconfig/db.database"));
////         String cData = zkClient.readData("/chaseyo/dev1.0.0/dbconfig/db.database");
////         System.out.println(cData);
        String path = "/chaseyo/dev1.0.0";
        List<String> children = zkClient.getChildren(path);
        System.out.println(children.size());
        for (String child : children) {
            System.out.println(child);
            List<String> childs = zkClient.getChildren(path + "/" + child);
            for (String s : childs) {
               Map data = zkClient.readData(path + "/" + child + "/" + s, new Stat());
                System.out.println(data);
            }
        }
    }

}
