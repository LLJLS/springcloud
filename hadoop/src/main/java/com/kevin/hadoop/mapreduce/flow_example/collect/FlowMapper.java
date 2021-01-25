package com.kevin.hadoop.mapreduce.flow_example.collect;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class FlowMapper extends Mapper<LongWritable, Text,Text,FlowBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(",");
        FlowBean flowBean = new FlowBean();
        flowBean.setNum1(Integer.valueOf(split[1]));
        flowBean.setNum2(Integer.valueOf(split[2]));
        flowBean.setNum3(Integer.valueOf(split[3]));
        flowBean.setNum4(Integer.valueOf(split[4]));
        Text text = new Text();
        text.set(split[0]);
        context.write(text,flowBean);
    }
}
