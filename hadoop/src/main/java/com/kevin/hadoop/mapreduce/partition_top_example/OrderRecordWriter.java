package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class OrderRecordWriter extends RecordWriter<OrderBean, NullWritable> {

    private List<FSDataOutputStream> fsDataOutputStreamList;
    private Map<String,FSDataOutputStream> fsDataOutputStreamMap;
    private static FSDataOutputStream fsDataOutputStream;
    private static FSDataOutputStream fsDataOutputStream1;
    private static FSDataOutputStream fsDataOutputStream2;
    private static FSDataOutputStream fsDataOutputStream3;
    private static FSDataOutputStream fsDataOutputStream4;

    public OrderRecordWriter(FSDataOutputStream fsDataOutputStream, FSDataOutputStream fsDataOutputStream1, FSDataOutputStream fsDataOutputStream2, FSDataOutputStream fsDataOutputStream3, FSDataOutputStream fsDataOutputStream4) {
        this.fsDataOutputStream = fsDataOutputStream;
        this.fsDataOutputStream1 = fsDataOutputStream1;
        this.fsDataOutputStream2 = fsDataOutputStream2;
        this.fsDataOutputStream3 = fsDataOutputStream3;
        this.fsDataOutputStream4 = fsDataOutputStream4;
    }

    public OrderRecordWriter() {
    }

    public OrderRecordWriter(List<FSDataOutputStream> fsDataOutputStreamList) {
        this.fsDataOutputStreamList = fsDataOutputStreamList;
    }

    public OrderRecordWriter(Map<String, FSDataOutputStream> fsDataOutputStreamMap) {
        this.fsDataOutputStreamMap = fsDataOutputStreamMap;
    }

    @Override
    public void write(OrderBean orderBean, NullWritable nullWritable) throws IOException, InterruptedException {
//        fsDataOutputStreamMap.get(orderBean.getClassify()).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));

        switch (orderBean.getClassify()) {
           case "computer":
//               fsDataOutputStreamList.get(0).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(0).write("\r\n".getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream.write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream.write("\r\n".getBytes(StandardCharsets.UTF_8));
               break;
           case "phone":
               fsDataOutputStream1.write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream1.write("\r\n".getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(1).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(1).write("\r\n".getBytes(StandardCharsets.UTF_8));
               break;
           case "food":
               fsDataOutputStream2.write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream2.write("\r\n".getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(2).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(2).write("\r\n".getBytes(StandardCharsets.UTF_8));
               break;
           case "game":
               fsDataOutputStream3.write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream3.write("\r\n".getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(3).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(3).write("\r\n".getBytes(StandardCharsets.UTF_8));
               break;
           default:
               fsDataOutputStream4.write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
               fsDataOutputStream4.write("\r\n".getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(4).write(orderBean.toString().getBytes(StandardCharsets.UTF_8));
//               fsDataOutputStreamList.get(4).write("\r\n".getBytes(StandardCharsets.UTF_8));
               break;
       }
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
//        fsDataOutputStreamList.stream().forEach(fsDataOutputStream -> {
//            IOUtils.closeStream(fsDataOutputStream);
//        });
        for (FSDataOutputStream fds:fsDataOutputStreamMap.values()) {
            IOUtils.closeStream(fds);
        }
    }
}
