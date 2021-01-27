package com.kevin.hadoop.mapreduce.customerOutputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class JobMain extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        // job
        Job job = Job.getInstance(super.getConf(), "customerOutputFormat_job");
        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerOutputFormat\\original"));
        // mapper
        job.setMapperClass(MyOutFormatMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);
        // suffer
        // reducer
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        // output
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerOutputFormat\\result");
        job.setOutputFormatClass(MyOutputFormat.class);
        MyOutputFormat.setOutputPath(job,path);
        LocalFileSystem fileSystem = FileSystem.getLocal(new Configuration());
        boolean exists = fileSystem.exists(path);
        if (exists) {
            fileSystem.delete(path,true);
        }
        fileSystem.close();
        // end
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        ToolRunner.run(new Configuration(),new JobMain(),args);
    }
}
