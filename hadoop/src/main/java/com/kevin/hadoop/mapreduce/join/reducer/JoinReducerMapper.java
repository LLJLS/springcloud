package com.kevin.hadoop.mapreduce.join.reducer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class JoinReducerMapper extends Mapper<LongWritable, Text,Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 1.判断数据来自哪个文件
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        String fileName = fileSplit.getPath().getName();
        if ("product.txt".equals(fileName)) {
            String[] split = value.toString().split(",");
            // 2.将k1,v1转为k2,v2,写入上下文中
            context.write(new Text(split[0]),value);
        } else if ("order.txt".equals(fileName)) {
            String[] split = value.toString().split(",");
            // 2.将k1,v1转为k2,v2,写入上下文中
            context.write(new Text(split[2]),value);
        }

    }
}
