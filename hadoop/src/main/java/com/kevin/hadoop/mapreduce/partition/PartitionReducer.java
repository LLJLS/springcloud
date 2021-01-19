package com.kevin.hadoop.mapreduce.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PartitionReducer extends Reducer<Text, NullWritable,Text,NullWritable> {

    public static enum Counter {
        TEST1,TEST2
    }
    @Override
    protected void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        // 方式二:通过枚举定义计数器
        context.getCounter(Counter.TEST1).increment(1l);
        context.write(key,NullWritable.get());

    }
}
