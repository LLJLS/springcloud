package com.kevin.spark.core.rdd.builder

import org.apache.spark.{SparkConf, SparkContext}

// 从外部存储（文件）创建RDD
object Spark02_RDD_File {

  def main(args: Array[String]): Unit = {

    // 准备环境
    val sf = new SparkConf().setMaster("local[*]").setAppName("RDD")
    // local[*]中的*标示cpu核数,如果只是local就表示是单核

    val sc = new SparkContext(sf)

    // 创建RDD
    // 从外部存储（文件）创建RDD
    // path可以是相对路径，也可以是绝对路径
    // 可以是目录
    // 可以使用通配符
    // 可以是分布式系统路径
    val rdd = sc.textFile("datas/1.txt")
    rdd.collect().foreach(println)
    // 停止环境
    sc.stop()
  }

}
