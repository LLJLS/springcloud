package com.kevin.hadoop.mapreduce.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 四个泛型解释：
 *  KEVIN:k2的类型
 *  VALUEIN:v2的类型
 *
 *  KEYOUT:k3的类型
 *  VALUEOUT:v3的类型
 *
 *  为了解决序列化臃肿的问题mapreduce写了一套自己的数据类型LongWritable,Text等
 */
public class WordCountReducer extends Reducer<Text,LongWritable,Text,LongWritable> {

    // reduce方法就是将k2-v2转为k3-v3,将k3-v3写入上下文中
    /*
    参数:
        key:        新k2
        values:     集合 新v2
        context:    表示上下文对象
     */
    /*
    如何将新k2-v2转为k3-v3
    新   k2      v2
        hello   <1,1,1>
        world   <1,1>
        hadoop  <1>

        k3      v3
        hello   3
        word    2
        hadoop  1
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        Long count = 0L;
        LongWritable longWritable = new LongWritable();
        //1.遍历集合，将集合中的数字相加,得到v3
        for (LongWritable value:values) {
            count += value.get();
        }
        //2.将k3和v3写入上下文中
        longWritable.set(count);
        context.write(key,longWritable);

    }
}
