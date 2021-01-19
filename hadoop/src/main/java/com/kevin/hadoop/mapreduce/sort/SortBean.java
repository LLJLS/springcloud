package com.kevin.hadoop.mapreduce.sort;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class SortBean implements WritableComparable<SortBean> {

    private String word;
    private int num;

    // 实现排序的规则
    /*
    规则：
        1.第一列(word)按照字典顺序进行排序。
        2.第一列相同的时候，第二列(num)按照升序进行排序。
     */
    @Override
    public int compareTo(SortBean o) {
        // 先对第一列排序
        int i = this.word.compareTo(o.word);
        if (0 == i) {
            return num-o.num;
        }
        return i;
    }

    // 实现序列化
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(word);
        dataOutput.writeInt(num);
    }

    // 实现反序列化
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.word = dataInput.readUTF();
        this.num = dataInput.readInt();
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return word+"\t"+num;
    }
}
