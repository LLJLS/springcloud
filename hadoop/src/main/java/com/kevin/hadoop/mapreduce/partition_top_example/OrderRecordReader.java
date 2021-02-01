package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class OrderRecordReader extends RecordReader<NullWritable, BytesWritable> {

    private Configuration configuration;
    private FileSplit fileSplit;
    private BytesWritable bytesWritable;
    private boolean processed = false;
    private FSDataInputStream fis;
    private FileSystem fileSystem;
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        configuration = taskAttemptContext.getConfiguration();
        fileSplit = (FileSplit) inputSplit;
    }

    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            fileSystem = FileSystem.get(configuration);
            fis = fileSystem.open(fileSplit.getPath());
            byte[] bytes = new byte[(int) fileSplit.getLength()];
            IOUtils.readFully(fis,bytes,0, (int) fileSplit.getLength());
            bytesWritable = new BytesWritable();
            bytesWritable.set(bytes,0,(int) fileSplit.getLength());
            processed = true;
            return true;
        }
        return false;

    }

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return bytesWritable;
    }

    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    @Override
    public void close() throws IOException {
        IOUtils.closeStream(fis);
        IOUtils.closeStream(fileSystem);
    }
}
