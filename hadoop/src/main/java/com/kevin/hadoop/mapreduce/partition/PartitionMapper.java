package com.kevin.hadoop.mapreduce.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PartitionMapper extends Mapper<LongWritable, Text,Text, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 方式一:定义计数器
        Counter counter = context.getCounter("MR_COUNTER", "partition_counter");
        // 每次执行该方法计数器就加1
        counter.increment(1L);
        context.write(value,NullWritable.get());
    }
}
