package com.kevin.hadoop.mapreduce.flow.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SortBean implements WritableComparable<SortBean> {

    private Integer num1;

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    @Override
    public int compareTo(SortBean o) {
        return o.getNum1()-this.getNum1();
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(num1);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        num1 = dataInput.readInt();
    }
}
