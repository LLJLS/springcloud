package com.kevin.spark.core.wc

import org.apache.spark.{SparkConf, SparkContext}

object WordCount3 {

  def main(args: Array[String]): Unit = {
    // Application
    // Spark框架
    // 建立和Spark框架的连接
    // JDBC:Connection
    val sparkConf = new SparkConf().setMaster("local").setAppName("WordCount")
    val sc = new SparkContext(sparkConf)
    // 执行业务操作
      // 1.读取文件，获取一行一行的数据
      val lines = sc.textFile("spark-core/data.txt")
      // 2.将一行数据进行拆分，形成一个一个的单词(分词)
      // 扁平化:将整体拆分成个体的操作
      val words = lines.flatMap(_.split(" "))
    val wordToOne = words.map(
      word => (word, 1)
    )
      // 3.将数据根据单词进行分组，便于统计
      // Spark框架提供了更多的功能，可以将分组和聚合使用一个方法实现
      // reduceByKey:相同的key的数据，可以对value进行reduce聚合
      // 自减原则
      val value = wordToOne.reduceByKey(_ + _)
      // 4.对分组后的数据进行转换

      // 5.将转换结果采集到控制台打印出来
      val array = value.collect()
      array.foreach(println)
    // 关闭连接
    sc.stop()
  }
}
