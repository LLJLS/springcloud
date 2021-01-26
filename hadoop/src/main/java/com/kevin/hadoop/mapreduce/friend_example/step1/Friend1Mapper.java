package com.kevin.hadoop.mapreduce.friend_example.step1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Friend1Mapper extends Mapper<LongWritable, Text,Text,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(":");
        String f1 = split[0];
        String[] f2 = split[1].toString().split(",");
        for (String f:f2) {
            context.write(new Text(f),new Text(f1));
        }
    }
}
