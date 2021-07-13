package com.kevin.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

/**
 * @className BioServer.java
 * @description //TODO
 * @author Administrator
 * @version 1.0
 * @date 2021/7/13 11:03
 */
public class BioServer {

    private static ExecutorService executorService = new ThreadPoolExecutor(1,1024,20000, TimeUnit.MICROSECONDS,new ArrayBlockingQueue<>(2));

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket accept = null;
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                System.out.println("服务器准备就绪，等待连接");
                accept = serverSocket.accept();
                Socket finalAccept = accept;
                Future<?> submit = executorService.submit(() -> {
                    String handler = handler(finalAccept);
                    return handler;
                });
                Object o = (String)submit.get();
                System.out.println(o);
            }
        } catch (IOException e) {
            try {
                accept.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String handler(Socket s) {
        try {
            byte[] bytes = new byte[1024];
            int read = s.getInputStream().read(bytes);
            System.out.println("客户端ip：" + s.getRemoteSocketAddress());
            System.out.println("线程：" + Thread.currentThread().getName() + Thread.currentThread().getId());
            System.out.println(new String(bytes,0,read));
            OutputStream outputStream = s.getOutputStream();
            outputStream.write("I am server".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {

        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }
}

