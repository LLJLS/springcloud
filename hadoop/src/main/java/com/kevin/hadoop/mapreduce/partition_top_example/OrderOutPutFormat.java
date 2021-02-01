package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class OrderOutPutFormat extends FileOutputFormat<OrderBean, NullWritable> {
//    private volatile static List<FSDataOutputStream> fsDataOutputStreamList = new CopyOnWriteArrayList<>();
    private volatile static Map<String,FSDataOutputStream> fsDataOutputStreamMap = new ConcurrentHashMap<>();

    @Override
    public RecordWriter<OrderBean, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {

        OrderRecordWriter orderRecordWriter = null;
        Configuration configuration = taskAttemptContext.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut\\computer\\order.txt"));
        FSDataOutputStream fsDataOutputStream1 = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut\\phone\\order.txt"));
        FSDataOutputStream fsDataOutputStream2 = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut\\food\\order.txt"));
        FSDataOutputStream fsDataOutputStream3 = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut\\game\\order.txt"));
        FSDataOutputStream fsDataOutputStream4 = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut\\default\\order.txt"));
//        orderRecordWriter = new OrderRecordWriter(fsDataOutputStreamList);
//        if (!fsDataOutputStreamList.isEmpty()) {
//            return orderRecordWriter;
//        }
        fsDataOutputStreamMap.put("computer",fsDataOutputStream);
        fsDataOutputStreamMap.put("phone",fsDataOutputStream1);
        fsDataOutputStreamMap.put("food",fsDataOutputStream2);
        fsDataOutputStreamMap.put("game",fsDataOutputStream3);
        fsDataOutputStreamMap.put("defalut",fsDataOutputStream4);
//        fsDataOutputStreamList.add(fsDataOutputStream);
//        fsDataOutputStreamList.add(fsDataOutputStream1);
//        fsDataOutputStreamList.add(fsDataOutputStream2);
//        fsDataOutputStreamList.add(fsDataOutputStream3);
//        fsDataOutputStreamList.add(fsDataOutputStream4);
        orderRecordWriter = new OrderRecordWriter(fsDataOutputStreamMap);
        return orderRecordWriter;
    }
}
