package com.kevin.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @className NioClient.java
 * @description //TODO
 * @author Administrator
 * @version 1.0
 * @date 2021/7/13 14:50
 */
public class NioClient {

    public static void main(String[] args) {
        try {
            Selector selector = Selector.open();
            SocketChannel client = SocketChannel.open();
            client.configureBlocking(false);
            client.connect(new InetSocketAddress("127.0.0.1",9999));
            client.register(selector,SelectionKey.OP_CONNECT);

            while (true) {
                int select = selector.select();
                if (select == 0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        channel.configureBlocking(false);
                        channel.write(ByteBuffer.wrap("hello,server".getBytes(StandardCharsets.UTF_8)));
                        channel.register(selector,SelectionKey.OP_READ);
                    }

                    if (key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        int len = channel.read(allocate);
                        System.out.println("客户端接收服务端信息：" + new String(allocate.array(),0,len));
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}