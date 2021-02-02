package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.ArrayList;

public class MyPartition extends Partitioner<OrderBean, NullWritable> {
    @Override
    public int getPartition(OrderBean orderBean, NullWritable nullWritable, int i) {
        if ("computer".equals(orderBean.getClassify())) {
            return 1;
        } else if ("phone".equals(orderBean.getClassify())) {
            return 2;
        } else if ("food".equals(orderBean.getClassify())) {
            return 3;
        } else if ("game".equals(orderBean.getClassify())) {
            return 4;
        } else {
            return 0;
        }
//       switch (orderBean.getClassify()){
//           case "computer":return 1;
//           case "phone":return 2;
//           case "food":return 3;
//           case "game":return 4;
//           default:return 0;
//       }
    }
}
