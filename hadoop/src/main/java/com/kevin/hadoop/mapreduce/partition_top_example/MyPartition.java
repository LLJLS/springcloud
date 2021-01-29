package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.ArrayList;

public class MyPartition extends Partitioner<OrderBean, NullWritable> {
    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int i) {
       switch (orderBean.getClassify()){
           case "computer":return 1;
           case "phone":return 2;
           case "food":return 3;
           case "game":return 4;
           default:return 0;
       }
    }
}
