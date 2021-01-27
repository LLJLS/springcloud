package com.kevin.hadoop.mapreduce.customerInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class MyRecordReader extends RecordReader<NullWritable, BytesWritable> {
    private Configuration configuration = null;
    private FileSplit fileSplit = null;
    private boolean processed = false;
    private BytesWritable bytesWritable = null;
    private FileSystem fileSystem = null;
    private FSDataInputStream fis = null;

    // 初始化
    @Override
    public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        // 获取Configuration对象
        configuration = taskAttemptContext.getConfiguration();
        // 获取文件的切片
        fileSplit = (FileSplit) inputSplit;
    }

    // 该方法用于获取k1和v1(重要)
    @Override
    public boolean nextKeyValue() throws IOException, InterruptedException {
        if (!processed) {
            // 1.获取源文件的字节输入流
            // 1.1.获取源文件的文件系统
            fileSystem = FileSystem.get(configuration);
            // 1.2.通过FileSystem获取文件字节输入流
            fis = fileSystem.open(fileSplit.getPath());
            // 2.读取源文件数据到普通的字节数组(byte[])
            byte[] bytes = new byte[(int) fileSplit.getLength()];
            IOUtils.readFully(fis,bytes,0, (int) fileSplit.getLength());
            // 3.将字节数组中的数据封装到BytesWritable,得到v1
            bytesWritable = new BytesWritable();
            bytesWritable.set(bytes,0, (int) fileSplit.getLength());
            processed = true;
            return true;
        }
        // 这里不要漏了
        return false;

    }

    // 返回k1
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    // 返回v1
    @Override
    public BytesWritable getCurrentValue() throws IOException, InterruptedException {
        return bytesWritable;
    }

    // 获取文件读取进度
    @Override
    public float getProgress() throws IOException, InterruptedException {
        return 0;
    }

    // 资源释放
    @Override
    public void close() throws IOException {
        fis.close();
        fileSystem.close();
    }
}
