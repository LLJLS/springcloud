package com.kevin.hadoop.mapreduce.customerOutputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.ArrayList;

public class MyOutputFormat extends FileOutputFormat<Text, NullWritable> {

    @Override
    public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // 1.读取目标文件的输出流
        Configuration configuration = taskAttemptContext.getConfiguration();
        FileSystem fileSystem = FileSystem.get(configuration);
        ArrayList<FSDataOutputStream> fsDataOutputStreams = new ArrayList<FSDataOutputStream>();
        FSDataOutputStream faker = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerOutputFormat\\result\\faker\\faker.txt"));
        FSDataOutputStream dopa = fileSystem.create(new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerOutputFormat\\result\\dopa\\dopa.txt"));
        fsDataOutputStreams.add(faker);
        fsDataOutputStreams.add(dopa);
        // 2.将输出流传给自定义的RecordWriter
        MyRecordWriter myRecordWriter = new MyRecordWriter(fsDataOutputStreams);
        return myRecordWriter;
    }
}
