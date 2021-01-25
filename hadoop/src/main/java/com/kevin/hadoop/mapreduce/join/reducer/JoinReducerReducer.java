package com.kevin.hadoop.mapreduce.join.reducer;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class JoinReducerReducer extends Reducer<Text,Text,Text,Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        String product = "";
        String order =  "";
        // 1.遍历集合，获取v3
        // 区分的规则要自己找
        for (Text text:values) {
            if (text.toString().startsWith("p")) {
                product = text.toString();
            } else {
                // 一个产品可以有多个订单
                order += text.toString();
            }
        }
        // 2.将k3,v3写入上下文
        context.write(key,new Text(product+"\t"+order));
    }
}
