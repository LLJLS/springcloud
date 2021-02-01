package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.math.BigDecimal;

public class OrderBean implements WritableComparable<OrderBean> {
    private String orderId;
    private String classify;
    private BigDecimal price;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return orderId+"-"+classify+"-"+price;
    }

    @Override
    public int compareTo(OrderBean o) {
        int i = 0;
        if (this.classify.compareTo(o.classify)==0) {
            i = -1 * this.price.subtract(o.price).intValue();
        }
        return i;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(orderId);
        dataOutput.writeUTF(classify);
        dataOutput.writeLong(price.longValue());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.orderId = dataInput.readUTF();
        this.classify = dataInput.readUTF();
        this.price = BigDecimal.valueOf(dataInput.readLong());
    }
}
