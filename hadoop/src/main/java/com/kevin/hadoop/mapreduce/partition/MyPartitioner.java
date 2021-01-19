package com.kevin.hadoop.mapreduce.partition;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 这里的输入类型与我们map的输出类型相同
 */
public class MyPartitioner extends Partitioner<Text, NullWritable> {
    /*
    返回值表示我们呢的数据要到哪个分区去
    返回值是一个分区的标记，标记所有相同的数据去到指定的分区
     */
    @Override
    public int getPartition(Text text, NullWritable nullWritable, int i) {
        String s = text.toString().split(",")[5];
        if ("\"王蒙梦\"".equals(s)) {
            return 1;
        } else if ("\"张三\"".equals(s)) {
            return 2;
        } else if ("\"测试账户\"".equals(s)) {
            return 3;
        }
        return 0;
    }
}
