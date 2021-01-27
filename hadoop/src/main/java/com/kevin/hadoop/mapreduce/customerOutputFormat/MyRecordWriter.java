package com.kevin.hadoop.mapreduce.customerOutputFormat;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MyRecordWriter extends RecordWriter<Text, NullWritable> {

    private List<FSDataOutputStream> fsDataOutputStreamList;

    public MyRecordWriter() {
    }

    public MyRecordWriter(List<FSDataOutputStream> fsDataOutputStreamList) {
        this.fsDataOutputStreamList = fsDataOutputStreamList;
    }

    @Override
    public void write(Text text, NullWritable nullWritable) throws IOException, InterruptedException {
        // 找出区分文件夹的标准
        String[] split = text.toString().split(":");
        String s = split[0];
        // 写入不同的文件夹中
        if ("faker".equals(s)) {
            fsDataOutputStreamList.get(0).write(text.toString().getBytes(StandardCharsets.UTF_8));
            fsDataOutputStreamList.get(0).write("\r\n".getBytes(StandardCharsets.UTF_8));
        } else if ("dopa".equals(s)) {
            fsDataOutputStreamList.get(1).write(text.toString().getBytes(StandardCharsets.UTF_8));
            fsDataOutputStreamList.get(1).write("\r\n".getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        fsDataOutputStreamList.stream().forEach(m->{
            IOUtils.closeStream(m);
        });
    }
}
