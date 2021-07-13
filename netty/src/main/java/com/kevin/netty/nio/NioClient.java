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
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(false);
            Selector selector = Selector.open();
            sc.register(selector,SelectionKey.OP_CONNECT);
            sc.connect(new InetSocketAddress("127.0.0.1",9999));

            while (true) {
                if (selector.select()>0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isConnectable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_READ);
                            }
                            channel.configureBlocking(false);
                            channel.register(selector,SelectionKey.OP_READ);
                            ByteBuffer wrap = ByteBuffer.wrap("hello,server".getBytes(StandardCharsets.UTF_8));
                            wrap.flip();
                            channel.write(wrap);
                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            allocate.flip();
                            int len = channel.read(allocate);
                            System.out.println("客户端接收服务端信息：" + new String(allocate.array(),0,len));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}