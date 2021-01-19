package com.kevin.hadoop.mapreduce.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

public class JobMain extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        int run = ToolRunner.run(new Configuration(),new JobMain(), args);
        System.exit(run);
    }
    @Override
    public int run(String[] strings) throws Exception {

        // job
        Job job = Job.getInstance(super.getConf(), "partitionerJob");
        // inputformat
        job.setInputFormatClass(TextInputFormat.class);
//        TextInputFormat.addInputPath(job,new Path("hdfs://node1:8020/partitionerCount"));
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\origin"));

        // mapper
        job.setMapperClass(PartitionMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        // shuffer，分区,排序，规约，分组
        // partition
        job.setPartitionerClass(MyPartitioner.class);
        job.setNumReduceTasks(4);

        // reduce
        job.setReducerClass(PartitionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // outputformat
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\result");
//        Path path = new Path("hdfs://node1:8020/partitionerCount_out");
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,path);

//        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node1:8020/"), new Configuration(), "root");
        FileSystem fileSystem = LocalFileSystem.get(new Configuration());
        boolean exists = fileSystem.exists(path);
        if (exists) {
            fileSystem.delete(path,true);
        }
        // end
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }
}