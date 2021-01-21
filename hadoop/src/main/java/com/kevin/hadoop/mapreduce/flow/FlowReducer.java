package com.kevin.hadoop.mapreduce.flow;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class FlowReducer extends Reducer<Text, FlowBean,Text, FlowBean> {

    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        Integer num1 = 0;
        Integer num2 = 0;
        Integer num3 = 0;
        Integer num4 = 0;
        for (FlowBean flowBean:values) {
            num1 += flowBean.getNum1();
            num2 += flowBean.getNum2();
            num3 += flowBean.getNum3();
            num4 += flowBean.getNum4();
        }
        FlowBean flowBean = new FlowBean();
        flowBean.setNum1(num1);
        flowBean.setNum2(num2);
        flowBean.setNum3(num3);
        flowBean.setNum4(num4);
        context.write(key,flowBean);
    }
}
