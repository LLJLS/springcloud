package com.kevin.hadoop.mapreduce.combiner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountCombiner extends Reducer<Text, LongWritable,Text,LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0l;
        LongWritable longWritable = new LongWritable();
        for (LongWritable l :values) {
            count += l.get();
        }
        longWritable.set(count);
        context.write(key,longWritable);
    }
}
