package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

public class OrderMapper extends Mapper<NullWritable, BytesWritable,OrderBean, NullWritable> {
    @Override
    protected void map(NullWritable key, BytesWritable value, Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit)context.getInputSplit();
        String s = new String(value.getBytes());
        String[] split1 = s.split("\r\n");
        Arrays.stream(split1).forEach(m->{
            String[] split = m.split(",");
            String orderId = split[0];
            String classify = split[1];
            String price = Strings.trimToNull(split[2]);
            OrderBean orderBean = new OrderBean();
            orderBean.setOrderId(orderId);
            orderBean.setClassify(classify);
            orderBean.setPrice(BigDecimal.valueOf(Long.valueOf(price)));
            try {
                context.write(orderBean,NullWritable.get());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
}
