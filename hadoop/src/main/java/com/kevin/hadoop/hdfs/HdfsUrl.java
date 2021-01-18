package com.kevin.hadoop.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HdfsUrl {
    public static void main(String[] args) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            // 1.注册hdfs的url
            URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
            // 2.获取文件输入流
            is = new URL("hdfs://172.16.30.101:8020/a.txt").openStream();
            // 3.获取文件输出流
            fos = new FileOutputStream(new File("D:/hello.txt"));
            // 4.实现文件拷贝
            IOUtils.copy(is, fos);
        } finally {
            // 5.关闭流
            IOUtils.closeQuietly(fos);
            IOUtils.closeQuietly(is);
        }


    }

}
