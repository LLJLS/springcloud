package com.kevin.hadoop.mapreduce.join.mapper;

import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class JoinMapperMapper extends Mapper<LongWritable,Text, Text,Text> {
    HashMap<String, String> map = new HashMap<>();
    // 第一件事情:将分布式缓存的小表数据读取到本地map集合(只需一次)
    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        // 1.获取分布式缓存文件列表
        URI[] uris = context.getCacheFiles();
        // 2.获取指定的分布式缓存文件的文件系统
        FileSystem fileSystem = FileSystem.get(uris[0], context.getConfiguration());
        // 3.获取文件的输入流
        FSDataInputStream inputStream = fileSystem.open(new Path(uris[0]));
        // 4.读取文件内容,将数据存入map集合
            // 4.1.将字节输入流转换成字符缓冲流
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            // 4.2.读取小表文件内容,以行为单位，并将读取的数据存入map集合
        String line = "";
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.toString().split(",");
            map.put(split[0],line);
        }
        // 5.关闭流
        bufferedReader.close();
        fileSystem.close();
    }


    // 第二件事情:对大表的处理业务逻辑，而且要实现大表和小表的join操作
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1.从行文本数据中获取商品的id
        String[] split = value.toString().split(",");
        String s = map.get(split[2]);
        // 2.在map集合中，将商品的id作为键，读取值，然后拼接字符串
        context.write(new Text(split[2]),new Text(s+"--"+value.toString()));
    }
}
