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
        try {
            Selector selector = Selector.open();
            ServerSocketChannel ssc = ServerSocketChannel.open();
            ssc.bind(new InetSocketAddress("127.0.0.1",9999));
            ssc.configureBlocking(false);
            ssc.register(selector,SelectionKey.OP_ACCEPT);
            System.out.println("服务器启动成功");

            while (true) {
                int s = selector.select();
                if(s==0) continue;
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        SocketChannel client = serverSocketChannel.accept();
                        client.configureBlocking(false);
                        client.register(selector,SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int len = client.read(byteBuffer);
                        System.out.println("服务端接收到客户端信息："+new String(byteBuffer.array(),0,len));

                        byteBuffer.flip();
                        client.write(ByteBuffer.wrap("hello,client".getBytes(StandardCharsets.UTF_8)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


