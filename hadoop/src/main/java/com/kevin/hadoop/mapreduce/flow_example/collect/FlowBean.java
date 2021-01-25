package com.kevin.hadoop.mapreduce.flow_example.collect;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FlowBean implements Writable {
    Integer num1;
    Integer num2;
    Integer num3;
    Integer num4;

    public Integer getNum1() {
        return num1;
    }

    public void setNum1(Integer num1) {
        this.num1 = num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public void setNum2(Integer num2) {
        this.num2 = num2;
    }

    public Integer getNum3() {
        return num3;
    }

    public void setNum3(Integer num3) {
        this.num3 = num3;
    }

    public Integer getNum4() {
        return num4;
    }

    public void setNum4(Integer num4) {
        this.num4 = num4;
    }

    @Override
    public String toString() {
        return num1+","+ num2+","+ num3+","+ num4;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(num1);
        dataOutput.writeInt(num2);
        dataOutput.writeInt(num3);
        dataOutput.writeInt(num4);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        num1= dataInput.readInt();
        num2= dataInput.readInt();
        num3= dataInput.readInt();
        num4= dataInput.readInt();
    }
}
