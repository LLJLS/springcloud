package com.kevin.hadoop.mapreduce.flow.sort;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class SortMapper extends Mapper<LongWritable, Text,SortBean, NullWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(",");

        SortBean sortBean = new SortBean();
        sortBean.setNum1(Integer.valueOf(split[1]));
        sortBean.setName(split[0]);
        context.write(sortBean,NullWritable.get());
    }
}
