package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// 文件的分区规则
object Spark02_RDD_File_Partion {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // minPartitions
    // hadoopFile
    // FileInputFormat
    // 用的是hadoop的分区规则
    // CRLF 回车换行 占两个字节
    val rdd = sc.textFile("datas/1.txt", 2)
    rdd.saveAsTextFile("output")
    // 停止环境
    sc.stop()
  }

}
