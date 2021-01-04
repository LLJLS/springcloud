package com.kevin.zookeeper;

import com.google.common.annotations.VisibleForTesting;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;

public class zookeeperApi {

    public static void main(String[] args) throws Exception {
        createZnode();
    }

    public static void createZnode() throws Exception {
        //1.定制重试策略
        int baseSleepTimeMs = 1000 ;// 重试的间隔时间
        int maxRetries = 1;// 重试的最大次数
        RetryPolicy retryPolicyTmp = new ExponentialBackoffRetry(baseSleepTimeMs,maxRetries);
        //2.获取一个客户端对象
        // 要连接的zookeeper服务器列表,也可以在hosts文件中设置别名
        String connectString = "172.16.30.101,172.16.30.103,172.16.30.104";
        int sessionTimeoutMs = 8000;// 会话的超时时间
        int connectionTimeoutMs = 8000;// 链接超时时间
        RetryPolicy retryPolicy = retryPolicyTmp;// 重试策略
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        //3.开启客户端
        client.start();
        //4.创建节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/hello2","world".getBytes(StandardCharsets.UTF_8));
        //5.关闭客户端
        client.close();
    }


}
