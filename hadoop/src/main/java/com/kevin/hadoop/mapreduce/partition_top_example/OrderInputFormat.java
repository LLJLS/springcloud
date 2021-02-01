package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;


import java.io.IOException;

public class OrderInputFormat extends FileInputFormat<NullWritable, BytesWritable> {


    @Override
    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        OrderRecordReader orderRecordReader = new OrderRecordReader();
        orderRecordReader.initialize(inputSplit,taskAttemptContext);
        return orderRecordReader;
    }

    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }
}
