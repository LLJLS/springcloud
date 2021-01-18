package com.kevin.hadoop.mapreduce.wordCcount;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * 四个泛型解释：
 *  KEVIN:k1的类型
 *  VALUEIN:v1的类型
 *
 *  KEYOUT:k2的类型
 *  VALUEOUT:v2的类型
 *
 *  为了解决序列化臃肿的问题mapreduce写了一套自己的数据类型LongWritable,Text等
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,LongWritable> {

    // map方法就是将k1-v1转为k2-v2
    /*
    参数:
        key:k1  行偏移量
        value:v2 每一行的文本数据
        context:表示上下文对象
     */
    /*
    如何将k1-v1转为k2-v2
    k1  v1
    0 hello,workd,hadoop
    15 hdfs,hive,hello

    k2      v2
    hello   1
    world   1
    hdfs    1

     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Text text = new Text();
        LongWritable longWritable = new LongWritable();
        // 1.将每一行的文本数据进行拆分
        String[] s = value.toString().split(" ");
        // 2.遍历数组，组装k2和v2
        for (String word:s) {
            // 3.将k2和v2写入上下文中
            text.set(word);
            longWritable.set(1);
            context.write(text,longWritable);
        }
    }
}
