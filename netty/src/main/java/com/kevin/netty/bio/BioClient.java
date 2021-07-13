package com.kevin.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @className BioClient.java
 * @description //TODO
 * @author Administrator
 * @version 1.0
 * @date 2021/7/13 11:21
 */
public class BioClient {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1",9999);
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello,server,I am client".getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        byte[] bytes = new byte[1024];
        int read = socket.getInputStream().read(bytes);
        System.out.println("接收数据："+new String(bytes,0,read));
    }
}