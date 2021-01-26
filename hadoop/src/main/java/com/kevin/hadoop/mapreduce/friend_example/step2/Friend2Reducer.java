package com.kevin.hadoop.mapreduce.friend_example.step2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Friend2Reducer extends Reducer<Text, Text,Text,Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer stringBuffer = new StringBuffer();
        for (Text t:values) {
            stringBuffer.append(t.toString()+",");
        }
        String substring = stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(","));
        context.write(key,new Text(substring));
    }
}
