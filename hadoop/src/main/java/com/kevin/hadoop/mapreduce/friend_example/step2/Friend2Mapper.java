package com.kevin.hadoop.mapreduce.friend_example.step2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Arrays;

public class Friend2Mapper extends Mapper<LongWritable, Text,Text,Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] split = value.toString().split("\t");
        String[] split1 = split[1].split(",");
        Arrays.sort(split1);
        for (int i=0;i<split1.length-1;i++) {
            for (int j=i+1;j<split1.length;j++) {
                context.write(new Text(split1[i]+"-"+split1[j]),new Text(split[0]));
            }
        }

    }
}
