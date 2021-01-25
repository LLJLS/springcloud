package com.kevin.hadoop.mapreduce.join.mapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;
import java.net.URL;

public class JoinMapperJob extends Configured implements Tool {
    @Override
    public int run(String[] strings) throws Exception {
        // job
        Job job = Job.getInstance(super.getConf(), "join_mapper");
        // cache
        job.addCacheFile(new URI("hdfs://node1:8020/distributed_cache/product.txt"));
        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\join\\mapper\\original"));
        // mapper
        job.setMapperClass(JoinMapperMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        // suffer
        // reducer
        // output
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\join\\mapper\\result");
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,path);
        FileSystem fileSystem = LocalFileSystem.get(new Configuration());
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
        int run = ToolRunner.run(new Configuration(), new JoinMapperJob(), args);
        System.exit(run);
    }
}
