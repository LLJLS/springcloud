package com.kevin.hadoop.mapreduce.friend_example.step1;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Friend1Reducer extends Reducer<Text,Text,Text,Text> {

    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuffer stringBuffer = new StringBuffer();
        Text text1 = new Text();
        for (Text text:values) {
            stringBuffer.append(text.toString()+",");
        }
        String substring = stringBuffer.toString().substring(0, stringBuffer.toString().lastIndexOf(","));
        text1.set(substring);
        context.write(key,text1);
    }
}
