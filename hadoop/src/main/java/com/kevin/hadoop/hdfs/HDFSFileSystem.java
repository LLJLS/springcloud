package com.kevin.hadoop.hdfs;


import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class HDFSFileSystem {
    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
//        mkdir();
//        getFileToLocal1();
//        getFileToLocal2();
//        uploadFile();
//        listFiles();
        uploadFileMerge();

    }

    private static void uploadFileMerge() throws IOException, URISyntaxException, InterruptedException {
        // 1.获取分布式文件系统
        FileSystem fileSystem = getFileSystem();
        // 2.获取hdfs大文件的输出流
        FSDataOutputStream fos = fileSystem.create(new Path("/big.txt"));
        // 3.获取本地文件系统
        LocalFileSystem local = FileSystem.getLocal(new Configuration());
        // 4.获取本地文件夹下所有文件的详情
        FileStatus[] fileStatuses = local.listStatus(new Path("D:\\test"));
        // 5.遍历每个文件，获取每个文件的输入流
        for (FileStatus fs:fileStatuses) {
            FSDataInputStream is = local.open(fs.getPath());
            // 6.将小文件的数据复制到大文件
            IOUtils.copy(is,fos);
            IOUtils.closeQuietly(is);
        }
        // 7.关闭流
        IOUtils.closeQuietly(fos);
        local.close();
        fileSystem.close();
    }

    private static void uploadFile() throws IOException, URISyntaxException, InterruptedException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //2.文件的拷贝
        fileSystem.copyFromLocalFile(new Path("file:///d:\\e.txt"),new Path("/e.txt"));
        //3.关闭流
        fileSystem.close();
    }

    private static void getFileToLocal2() throws IOException, URISyntaxException, InterruptedException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //4.文件的拷贝
        fileSystem.copyToLocalFile(new Path("/b.txt"),new Path("D://b.txt"));
        //5.关闭流
        fileSystem.close();
    }

    private static void getFileToLocal1() throws IOException, URISyntaxException, InterruptedException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //2.获取HDFS的输入流
        FSDataInputStream fis = fileSystem.open(new Path("/c.txt"));
        //3.获取本地文件的输出流
        FileOutputStream fos = new FileOutputStream(new File("D:\\world.txt"));
        //4.文件的拷贝
        IOUtils.copy(fis,fos);
        IOUtils.closeQuietly(fis);
        //5.关闭流
        fileSystem.close();
    }


    private static void mkdir() throws IOException, URISyntaxException, InterruptedException {
        //1.获取FileSystem实例
        FileSystem fileSystem = getFileSystem();
        //2.调用listFiles获取“/”目录下所有的文件信息
        boolean isSuccess = fileSystem.mkdirs(new Path("/test/"));
        fileSystem.close();
    }

    private static void listFiles() throws IOException, URISyntaxException, InterruptedException {
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

    private static FileSystem getFileSystem2() throws IOException, URISyntaxException, InterruptedException {
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node1:8020"),new Configuration(),"root");
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

    private static FileSystem getFileSystem() throws IOException, URISyntaxException, InterruptedException {
        return getFileSystem2();
    }



}
