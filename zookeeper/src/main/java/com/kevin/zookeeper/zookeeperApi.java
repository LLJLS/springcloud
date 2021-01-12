package com.kevin.zookeeper;

import ch.qos.logback.core.util.TimeUtil;
import com.google.common.annotations.VisibleForTesting;
import io.netty.util.internal.ConstantTimeUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class zookeeperApi {

    public static void main(String[] args) throws Exception {
        handle();
    }

    public static void handle() throws Exception {
        //1.定制重试策略
        int baseSleepTimeMs = 1000 ;// 重试的间隔时间
        int maxRetries = 1;// 重试的最大次数
        RetryPolicy retryPolicyTmp = new ExponentialBackoffRetry(baseSleepTimeMs,maxRetries);
        //2.获取一个客户端对象
        //要连接的zookeeper服务器列表,也可以在hosts文件中设置别名
        String connectString = "172.16.30.101:2181,172.16.30.103,172.16.30.104";
        int sessionTimeoutMs = 8000;// 会话的超时时间
        int connectionTimeoutMs = 20000;// 链接超时时间
        RetryPolicy retryPolicy = retryPolicyTmp;// 重试策略
        CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs, connectionTimeoutMs, retryPolicy);
        //3.开启客户端
        client.start();
        //4.操作
        //创建节点,根据withMode的枚举参数来改变创建的节点类型
//        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/hello4","world4".getBytes(StandardCharsets.UTF_8));
        //设置节点数据
//        client.setData().forPath("/hello4","hello4".getBytes(StandardCharsets.UTF_8));
        //获取节点数据
//        byte[] bytes = client.getData().forPath("/hello4");
//        String s = new String(bytes);
//        System.out.println(s);
        //watch机制,CuratorCache取代了TreeCache
        CuratorCache curatorCache = CuratorCache.builder(client, "/watch").build();
        curatorCache.listenable().addListener(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldData, ChildData data) {
                 if (!oldData.equals(data)) {
                     switch (type) {
                         case NODE_CHANGED:
                             try {
                                 System.out.println("节点改变了");
//                                 client.setData().forPath(oldData.getPath(),data.getData());
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             break;
                         case NODE_CREATED:
                             try {
                                 client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(data.getPath(),data.getData());
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             break;
                         case NODE_DELETED:
                             try {
                                 client.delete().forPath(data.getPath());
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }
                             break;
                         default:
                             throw new IllegalStateException("Unexpected value: " + type);
                     }
                 }
            }
        });
        curatorCache.start();// 开始监听
        TimeUnit.MINUTES.sleep(60);

        //5.关闭客户端
        client.close();
    }


}
