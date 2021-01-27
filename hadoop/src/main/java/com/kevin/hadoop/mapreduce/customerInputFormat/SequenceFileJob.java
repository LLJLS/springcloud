package com.kevin.hadoop.mapreduce.customerInputFormat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class SequenceFileJob extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        // job
        Job job = Job.getInstance(super.getConf(), "sequenceFile_job");
        // input
        job.setInputFormatClass(MyInputFormat.class);
        MyInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerInputFormat\\original"));
        // mapper
        job.setMapperClass(SequenceFileMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(BytesWritable.class);
        // suffer
        // reducer，不需要设置reducer类，但是必须设置数据类型，不然会报错
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(BytesWritable.class);
        // output
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\customerInputFormat\\result");
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setOutputPath(job,path);
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
        int run = ToolRunner.run(new Configuration(), new SequenceFileJob(), args);
        System.exit(run);
    }
}
