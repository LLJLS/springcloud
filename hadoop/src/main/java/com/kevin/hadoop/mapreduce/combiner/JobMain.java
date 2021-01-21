package com.kevin.hadoop.mapreduce.combiner;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

public class JobMain extends Configured implements Tool {
    // 该方法用于指定一个job任务
    @Override
    public int run(String[] strings) throws Exception {
        // 1.创建一个job任务对象
        Job job = Job.getInstance(super.getConf(), "wordcount");
        // 如果打包运行出错需要加该配置
        job.setJarByClass(JobMain.class);
        // 2.配置job任务对象(八个步骤)
        // Step1.指定文件的读取方式和读取路径
        job.setInputFormatClass(TextInputFormat.class);
//        TextInputFormat.addInputPath(job,new Path("hdfs://node1:8020/wordcount"));
//        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\wordCount\\original"));
        TextInputFormat.addInputPath(job,new Path("file:///C:\\Users\\rr\\Desktop\\test\\combiner\\original"));

        // Step2.指定的Map阶段的处理方式和数据类型
        job.setMapperClass(WordCountMapper.class);
        // 设置Map阶段k2的类型
        job.setMapOutputKeyClass(Text.class);
        // 设置Map阶段v2的类型
        job.setMapOutputValueClass(LongWritable.class);

        // Step3、4、5、6 采用默认方式处理
        // 规约,因为和Reducer完全一样，完全可以直接使用Reducer嘛
//        job.setCombinerClass(WordCountReducer.class);
        job.setCombinerClass(WordCountCombiner.class);

        // Step7.指定Reduce阶段的处理方式和数据类型
        job.setReducerClass(WordCountReducer.class);
        // 设置k3类型
        job.setOutputKeyClass(Text.class);
        // 设置v3类型
        job.setOutputValueClass(LongWritable.class);

        // Step8.设置输出类型和输出路径
//        Path path = new Path("hdfs://node1:8020/wordcount_out");
//        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\wordCount\\result");
        Path path = new Path("file:///C:\\Users\\rr\\Desktop\\test\\combiner\\result");
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job,path);
        // 获取FileSystem
//        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node1:8020"), new Configuration(),"root");
        FileSystem fileSystem = LocalFileSystem.get(super.getConf());
        // 判断目录是否存在
        boolean exists = fileSystem.exists(path);
        if (exists) {
            // 删除目标目录
            fileSystem.delete(path,true);
        }


        // 等待任务结束
        boolean b = job.waitForCompletion(true);
        return b?0:1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new Configuration();

        // 启动job任务
        int run = ToolRunner.run(configuration, new JobMain(), args);
        System.exit(run);
    }
}
