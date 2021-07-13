package com.kevin.netty.nio;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Administrator
 * @version 1.0
 * @className NioServer.java
 * @description //TODO
 * @date 2021/7/13 14:50
 */
public class NioServer {

    public static void main(String[] args) {
        ServerSocketChannel ssc = null;
        try {
            ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("127.0.0.1",9999));
            ssc.configureBlocking(false);
            System.out.println("服务器正常启动,等待接收请求");

            Selector selector = Selector.open();
            ssc.register(selector,SelectionKey.OP_ACCEPT);

            while (true) {
                if (selector.select()>0) {
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                            SocketChannel sc = channel.accept();
                            sc.configureBlocking(false);
                            sc.register(selector,SelectionKey.OP_READ);
                            key.interestOps(SelectionKey.OP_ACCEPT);
                        } else if (key.isReadable()) {
                            SocketChannel channel = (SocketChannel) key.channel();
                            channel.configureBlocking(false);
                            ByteBuffer allocate = ByteBuffer.allocate(1024);
                            allocate.flip();
                            int len = channel.read(allocate);
                            System.out.println("服务端接收到客户端的信息：" + new String(allocate.array(),0,len));

                            ByteBuffer wrap = ByteBuffer.wrap("hello,client".getBytes(StandardCharsets.UTF_8));
                            channel.write(wrap);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


