package com.kevin.hadoop;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HDFSFileSystem {
    public static void main(String[] args) throws IOException, URISyntaxException {
        mkdir();
        listFiles();
    }

    private static void mkdir() throws IOException, URISyntaxException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //2.调用listFiles获取“/”目录下所有的文件信息
        boolean isSuccess = fileSystem.mkdirs(new Path("/test/"));
        fileSystem.close();
    }

    private static void listFiles() throws IOException, URISyntaxException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //2.调用listFiles获取“/”目录下所有的文件信息
        RemoteIterator<LocatedFileStatus> locatedFileStatusRemoteIterator = fileSystem.listFiles(new Path("/"), true);
        //3.遍历迭代器
        while (locatedFileStatusRemoteIterator.hasNext()) {
            LocatedFileStatus next = locatedFileStatusRemoteIterator.next();
            System.out.println(next.getPath().toString());
        }
        fileSystem.close();
    }

    private static FileSystem getFileSystem1() throws IOException {
        //1.创建Configuration对象
        Configuration configuration = new Configuration();
        //2.设置文件系统类型
        configuration.set("fs.defaultFS","hdfs://node1:8020");
        //3.获取指定的文件系统
        FileSystem fileSystem = FileSystem.get(configuration);
        //4.输出
        System.out.println(fileSystem);
        return fileSystem;
    }

    private static FileSystem getFileSystem2() throws IOException, URISyntaxException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node1:8020"),new Configuration());
        System.out.println(fileSystem);
        return fileSystem;
    }

    private static FileSystem getFileSystem3() throws IOException {
        //1.创建Configuration对象
        Configuration configuration = new Configuration();
        //2.设置文件系统类型
        configuration.set("fs.defaultFS","hdfs://node1:8020");
        //3.获取指定的文件系统
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        //4.输出
        System.out.println(fileSystem);
        return fileSystem;
    }

    private static FileSystem getFileSystem4() throws IOException, URISyntaxException {
        FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://node1:8020"),new Configuration());
         System.out.println(fileSystem);
         return fileSystem;
    }

    private static FileSystem getFileSystem() throws IOException, URISyntaxException {
        return getFileSystem1();
    }



}
