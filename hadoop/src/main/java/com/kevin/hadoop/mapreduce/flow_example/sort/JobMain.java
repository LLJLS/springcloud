package com.kevin.hadoop.mapreduce.flow_example.sort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class JobMain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        Job job = Job.getInstance(super.getConf(), "flow_sort");
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\flow\\collect\\result"));

        job.setMapperClass(SortMapper.class);
        job.setMapOutputKeyClass(SortBean.class);
        job.setOutputValueClass(NullWritable.class);

        job.setReducerClass(SortReducer.class);
        job.setOutputKeyClass(SortBean.class);
        job.setOutputValueClass(NullWritable.class);

        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\flow\\sort");
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,path);

        FileSystem fileSystem = LocalFileSystem.get(new Configuration());
        boolean exists = fileSystem.exists(path);
        if (exists) {
            fileSystem.delete(path,true);
        }
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new JobMain(), args);
        System.exit(run);
    }
}
