package com.kevin.hadoop.mapreduce.flow.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class Partition extends Partitioner<Text, NullWritable> {


    @Override
    public int getPartition(Text text, NullWritable nullWritable, int i) {
        String[] split = text.toString().split("\t");
        String s = split[0];
        if ("faker".equals(s)) {
            return 0;
        } else if ("dopa".equals(s)) {
            return 1;
        } else if ("shy".equals(s)) {
            return 2;
        } else if ("uzi".equals(s)) {
            return 3;
        } else {
            return 4;
        }
    }
}
