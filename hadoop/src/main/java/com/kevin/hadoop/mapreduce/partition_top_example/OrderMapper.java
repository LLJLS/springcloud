package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.math.BigDecimal;

public class OrderMapper extends Mapper<LongWritable, Text,OrderBean, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split(",");
        String orderId = split[0];
        String classify = split[1];
        String price = split[2];
        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(orderId);
        orderBean.setClassify(classify);
        orderBean.setPrice(BigDecimal.valueOf(Long.valueOf(price)));
        context.write(orderBean,NullWritable.get());
    }
}
