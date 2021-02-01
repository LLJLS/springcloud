package com.kevin.hadoop.mapreduce.partition_top_example;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class OrderJob extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        // job
        Job job = Job.getInstance(super.getConf(), "partition_top_job");
        // input
        job.setInputFormatClass(OrderInputFormat.class);
        OrderInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\original"));
        // mapper
        job.setMapperClass(OrderMapper.class);
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);
        // suffer
        job.setPartitionerClass(MyPartition.class);
        job.setNumReduceTasks(5);
        // reducer
        job.setReducerClass(OrderReducer.class);
        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);
        // output
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\partition_top_example\\resut");
        job.setOutputFormatClass(OrderOutPutFormat.class);
        OrderOutPutFormat.setOutputPath(job,path);
        // fileSystem
        LocalFileSystem fileSystem = LocalFileSystem.getLocal(super.getConf());
        boolean exists = fileSystem.exists(path);
        if (exists) {
            fileSystem.delete(path,true);
        }
        // end
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(), new OrderJob(), args);
        System.exit(run);
    }
}
