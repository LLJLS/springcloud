package com.kevin.hadoop.mapreduce.flow.partition;

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

public class PartitionJob extends Configured implements Tool {

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(),new PartitionJob(),args);
    }

    @Override
    public int run(String[] strings) throws Exception {
        // job
        Job job = Job.getInstance(super.getConf(), "flow_partition");

        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\flow\\collect\\result"));

        // mapper
        job.setMapperClass(PartitionMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);

        // suffer
        job.setPartitionerClass(Partition.class);
        job.setNumReduceTasks(5);

        // reducer
        job.setReducerClass(PartitionReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        // output
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\flow\\partition\\result");
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,path);

        FileSystem fileSystem = LocalFileSystem.getLocal(new Configuration());
        boolean exists = fileSystem.exists(path);
        if (exists) {
            fileSystem.delete(path,true);
        }
        // end
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }
}
